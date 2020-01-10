package ru.c57m7a.dbr.db.vm.event

import com.sun.jdi.event.*
import java.util.*
import javax.persistence.Column
import javax.persistence.MappedSuperclass
import javax.persistence.Temporal
import javax.persistence.TemporalType

@MappedSuperclass
abstract class TEvent(@Suppress("UNUSED_PARAMETER") e: Event) {
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "time", nullable = false)
    val time = Date()

/*
    companion object {
        operator fun get(e: Event) = when (e) {
            is AccessWatchpointEvent -> TFieldEvent[e]
            is BreakpointEvent -> TBreakpointEvent(e)
            is ClassPrepareEvent -> TType.(e)
            is ClassUnloadEvent -> TClassUnloadEvent(e)
            is ExceptionEvent -> TExceptionEvent(e)
            is LocatableEvent -> TLocatableEvent(e)
            is MethodEntryEvent -> TMethodEntryEvent(e)
            is MethodExitEvent -> TMethodExitEvent(e)
            is ModificationWatchpointEvent -> TModificationWatchpointEvent(e)
            is MonitorContendedEnterEvent -> TMonitorContendedEnterEvent(e)
            is MonitorContendedEnteredEvent -> TMonitorContendedEnteredEvent(e)
            is MonitorWaitEvent -> TMonitorWaitEvent(e)
            is MonitorWaitedEvent -> TMonitorWaitedEvent(e)
            is ThreadDeathEvent -> TThreadEvent.TThreadDeathEvent(e)
            is ThreadStartEvent -> TThreadEvent.TThreadStartEvent(e)
            is WatchpointEvent -> TWatchpointEvent(e)
        }
    }
*/
}