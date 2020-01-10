package ru.c57m7a.dbr.db.vm

import com.sun.jdi.AbsentInformationException
import com.sun.jdi.ClassNotLoadedException
import com.sun.jdi.Method
import ru.c57m7a.dbr.db.vm.TType.TReferenceType
import ru.c57m7a.dbr.db.vm.event.TMethodInvocationEvent
import ru.c57m7a.dbr.utils.ObjectCache
import ru.c57m7a.dbr.utils.tryOrNull
import javax.persistence.*

@Entity
@Table(name = "method")
class TMethod private constructor(@Transient private val method: Method) {
    @Id
    @GeneratedValue
    @Column(name = "method_id")
    val id = 0

    /* TypeComponent */
    @Column(name = "name", nullable = false)
    val name: String = method.name()

    @Column(name = "signature", nullable = false)
    val signature: String = method.signature()

    @Column(name = "generic_signature", nullable = true)
    val genericSignature: String? = method.genericSignature()

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "declaring_reference_type_id", nullable = true)
    var declaringType: TReferenceType? = null

    @Column(name = "is_static", nullable = false)
    val isStatic = method.isStatic

    @Column(name = "is_final", nullable = false)
    val isFinal = method.isFinal

    @Column(name = "is_synthetic", nullable = false)
    val isSynthetic = method.isSynthetic

    /* Locatable */
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "location_id", nullable = true)
    var location: TLocation? = null

    /* Method */
    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "return_type_id", nullable = true)
    val returnType = tryOrNull<TType, ClassNotLoadedException> { TType[method.returnType()] }

    @Column(name = "is_abstract", nullable = false)
    val isAbstract = method.isAbstract

    @Column(name = "is_synchronized", nullable = false)
    val isSynchronized = method.isSynchronized

    @Column(name = "is_native", nullable = false)
    val isNative = method.isNative

    @Column(name = "is_varargs", nullable = false)
    val isVarArgs = method.isVarArgs

    @Column(name = "is_bridge", nullable = false)
    val isBridge = method.isBridge

    @Column(name = "is_constructor", nullable = false)
    val isConstructor = method.isConstructor

    @Column(name = "is_static_initializer", nullable = false)
    val isStaticInitializer = method.isStaticInitializer

    @Column(name = "is_obsolete", nullable = false)
    val isObsolete = method.isObsolete

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "declaringMethod")
    val variables = tryOrNull<List<TLocalVariable>, AbsentInformationException> {
        method.variables().map { TLocalVariable[it].apply { declaringMethod = this@TMethod } }
    }

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "method")
    val methodInvocations = mutableListOf<TMethodInvocationEvent>()

    companion object : ObjectCache<Method, TMethod>(::TMethod)
}