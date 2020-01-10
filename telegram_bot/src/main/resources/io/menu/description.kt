package ru.c57m7a.dbr.io.menu

import ru.c57m7a.dbr.io.MenuItem
import com.sun.jdi.VirtualMachine
import kotlin.reflect.KFunction1

private val capabilities = VirtualMachine::class.members
        .filter { it.name.startsWith("can") }
        .filterIsInstance<KFunction1<VirtualMachine, Boolean>>()

internal fun description(vm: VirtualMachine) =
    MenuItem("VM description and capabilities") {
        val capabilitiesString = capabilities.joinToString(separator = "\n") { property ->
            "${property.name}: ${property(vm)}"
        }

        "${vm.description()}\n\n$capabilitiesString"
    }