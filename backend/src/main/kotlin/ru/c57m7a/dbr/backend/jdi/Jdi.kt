package ru.c57m7a.dbr.backend.jdi

import com.sun.jdi.Bootstrap
import com.sun.jdi.VirtualMachine
import com.sun.jdi.event.Event
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.produce

val virtualMachineManager = Bootstrap.virtualMachineManager()!!
val connector = virtualMachineManager
    .attachingConnectors()
    .first { "hostname" in it.defaultArguments().keys }

fun VirtualMachine.events(timeout: Long = 0) = GlobalScope.produce<Event> {
    val eventQueue = eventQueue()
    try {
        val events = eventQueue.remove(timeout)

        for (event in events) {
            send(event)
        }
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
}