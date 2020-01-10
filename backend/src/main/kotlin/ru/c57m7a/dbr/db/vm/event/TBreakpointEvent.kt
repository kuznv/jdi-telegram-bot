package ru.c57m7a.dbr.db.vm.event

import com.sun.jdi.event.BreakpointEvent
import ru.c57m7a.dbr.db.vm.TLocation
import ru.c57m7a.dbr.db.vm.TValue.TObjectReference.TThreadReference
import javax.persistence.*

@Entity
@Table(name = "breakpoint_event")
class TBreakpointEvent(e: BreakpointEvent) : TEvent(e) {
    @Id
    @GeneratedValue
    @Column(name = "breakpoint_event_id")
    val id = 0

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "location_id", nullable = false)
    val location = TLocation[e.location()]

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "thread_id", nullable = false)
    val thread = TThreadReference[e.thread()]
}