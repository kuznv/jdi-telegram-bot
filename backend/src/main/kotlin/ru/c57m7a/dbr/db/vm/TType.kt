package ru.c57m7a.dbr.db.vm

import com.sun.jdi.*
import ru.c57m7a.dbr.utils.ForeignKeyObjectCache
import ru.c57m7a.dbr.utils.ObjectCache
import ru.c57m7a.dbr.utils.tryOrNull
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "type")
@Inheritance(strategy = InheritanceType.JOINED)
open class TType private constructor(
        @Column(name = "name", nullable = false)
        val name: String,

        @Column(name = "signature", nullable = false)
        val signature: String
) {
    constructor(type: Type) : this(type.name(), type.signature())

    companion object : ObjectCache<Type, TType>(::TType) {
        val VOID_TYPE = TType("void", "V")
        val BOOLEAN_TYPE = TType("boolean", "Z")
        val BYTE_TYPE = TType("byte", "B")
        val CHAR_TYPE = TType("char", "C")
        val SHORT_TYPE = TType("short", "S")
        val INT_TYPE = TType("int", "I")
        val LONG_TYPE = TType("long", "J")
        val FLOAT_TYPE = TType("float", "F")
        val DOUBLE_TYPE = TType("double", "D")
    }

    @Id
    @GeneratedValue
    @Column(name = "type_id")
    val id = 0

    @Entity
    @Table(name = "reference_type")
    @Inheritance(strategy = InheritanceType.JOINED)
    @PrimaryKeyJoinColumn(name = "reference_type_id", referencedColumnName = "type_id")
    sealed class TReferenceType(referenceType: ReferenceType) : TType(referenceType) {
        companion object {
            operator fun get(referenceType: ReferenceType) = when (referenceType) {
                is ClassType -> TClassType[referenceType]
                is InterfaceType -> TInterfaceType[referenceType]
                is ArrayType -> TArrayType[referenceType]
                else -> throw ClassCastException("Unknown declaringType type ${referenceType::class.qualifiedName}")
            }
        }

        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "loaded_at", nullable = false)
        val loadedAt = Date()

        /* Accessible */
        @Column(name = "is_private", nullable = false)
        val isPrivate = referenceType.isPrivate

        @Column(name = "is_package_private", nullable = false)
        val isPackagePrivate = referenceType.isPackagePrivate

        @Column(name = "is_protected", nullable = false)
        val isProtected = referenceType.isProtected

        @Column(name = "is_public", nullable = false)
        val isPublic = referenceType.isPublic

        /* ReferenceType */
        @ManyToOne(cascade = [CascadeType.ALL], optional = true)
        @JoinColumn(name = "class_loader_id")
        var classLoader: TValue.TObjectReference.TClassLoaderReference? = null

        @Column(name = "source_name", nullable = true)
        val sourceName = tryOrNull<String, AbsentInformationException> { referenceType.sourceName() }

        @Column(name = "source_debug_extension", nullable = true)
        val sourceDebugExtension = tryOrNull<String, AbsentInformationException> { referenceType.sourceDebugExtension() }

        @Column(name = "is_static", nullable = false)
        val isStatic = referenceType.isStatic

        @Column(name = "is_abstract", nullable = false)
        val isAbstract = referenceType.isAbstract

        @Column(name = "is_final", nullable = false)
        val isFinal = referenceType.isFinal

        @Column(name = "is_prepared", nullable = false)
        val isPrepared = referenceType.isPrepared

        @Column(name = "is_verified", nullable = false)
        val isVerified = referenceType.isVerified

        @Column(name = "is_initialized", nullable = false)
        val isInitialized = referenceType.isInitialized

        @Column(name = "is_failed_to_initialize", nullable = false)
        val isFailedToInitialize = referenceType.failedToInitialize()

        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "declaringType")
        val fields = tryOrNull<List<TField>, ClassNotPreparedException> { referenceType.fields().map { TField[it].also { it.declaringType = this } } }

        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "declaringType")
        val methods = tryOrNull<List<TMethod>, ClassNotPreparedException> { referenceType.methods().map { TMethod[it].also { it.declaringType = this } } }

        @ManyToOne(cascade = [CascadeType.ALL], optional = true)
        @JoinColumn(name = "base_reference_type_id")
        var baseType: TReferenceType? = null

        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "baseType")
        val nestedTypes = referenceType.nestedTypes().map { TReferenceType[it].also { it.baseType = this } }

        @OneToOne(cascade = [CascadeType.ALL], mappedBy = "reflectedType", optional = true)
        var classObject: TValue.TObjectReference.TClassObjectReference? = null

//        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "declaringType")
//        val lineLocations = tryOrNull<List<TLocation>, ClassNotPreparedException> { referenceType.allLineLocations().map { TLocation[it].also { it.declaringType = this } } }

        open class SignatureObjectCache<in K : ReferenceType, V : TReferenceType> protected constructor(default: (K) -> V)
            : ForeignKeyObjectCache<K, String, V>(default, ReferenceType::signature)

        @Entity
        @Table(name = "class_type")
        @PrimaryKeyJoinColumn(name = "class_type_id", referencedColumnName = "reference_type_id")
        class TClassType private constructor(classType: ClassType) : TReferenceType(classType) {
            companion object : SignatureObjectCache<ClassType, TClassType>(
                TReferenceType::TClassType
            )

            @ManyToOne(cascade = [CascadeType.ALL], optional = true)
            @JoinColumn(name = "superclass_class_type_id")
            var superclass: TClassType? = null

            @ManyToMany(cascade = [CascadeType.ALL], mappedBy = "implementors")
            val interfaces = tryOrNull<List<TInterfaceType>, ClassNotPreparedException> { classType.interfaces().map { TInterfaceType[it].also { it.implementors += this } } }

            @OneToMany(cascade = [CascadeType.ALL], mappedBy = "superclass")
            val subclasses = tryOrNull<List<TClassType>, ClassNotPreparedException> { classType.subclasses().map { TClassType[it].also { it.superclass = this } } }

            @Column(name = "is_enum", nullable = false)
            val isEnum = classType.isEnum
        }

        @Entity
        @Table(name = "interface_type")
        @PrimaryKeyJoinColumn(name = "interface_type_id", referencedColumnName = "reference_type_id")
        class TInterfaceType private constructor(interfaceType: InterfaceType) : TReferenceType(interfaceType) {
            companion object : SignatureObjectCache<InterfaceType, TInterfaceType>(
                TReferenceType::TInterfaceType
            )

            @ManyToMany(cascade = [CascadeType.ALL])
            @JoinTable(
                    name = "interface__superinterface",
                    joinColumns = [JoinColumn(name = "interface_id")],
                    inverseJoinColumns = [(JoinColumn(name = "superinterface_id"))]
            )
            val superinterfaces = interfaceType.superinterfaces().map { TInterfaceType[it].also { it.subinterfaces += this } }

            @ManyToMany(cascade = [CascadeType.ALL], mappedBy = "superinterfaces")
            val subinterfaces = mutableListOf<TInterfaceType>()

            @ManyToMany(cascade = [CascadeType.ALL])
            @JoinTable(
                    name = "interface__implementor",
                    joinColumns = [JoinColumn(name = "interface_id")],
                    inverseJoinColumns = [(JoinColumn(name = "implementor_class_type_id"))]
            )
            val implementors = mutableListOf<TClassType>()
        }

        @Entity
        @Table(name = "array_type")
        @PrimaryKeyJoinColumn(name = "array_type_id", referencedColumnName = "reference_type_id")
        class TArrayType private constructor(arrayType: ArrayType) : TReferenceType(arrayType) {
            companion object : SignatureObjectCache<ArrayType, TArrayType>(
                TReferenceType::TArrayType
            )

            @Column(name = "component_signature", nullable = false)
            val componentSignature: String = arrayType.componentSignature()

            @Column(name = "component_type_name", nullable = false)
            val componentTypeName: String = arrayType.componentTypeName()
        }
    }
}