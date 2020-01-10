package ru.c57m7a.dbr.io.menu

import ru.c57m7a.dbr.io.MenuItem
import com.sun.jdi.VirtualMachine

internal fun classes(vm: VirtualMachine) =
    MenuItem("Classes") {
        vm.allClasses().joinToString(separator = "\n")
    }