package ru.c57m7a.dbr.db.vm.event

import com.sun.jdi.event.ExceptionEvent
import ru.c57m7a.dbr.db.vm.TLocation
import ru.c57m7a.dbr.db.vm.TValue
import javax.persistence.*

@Entity
@Table(name = "exception_event")
class TExceptionEvent(e: ExceptionEvent) : TEvent(e) {
    @Id
    @GeneratedValue
    @Column(name = "exception_event_id")
    val id = 0

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "thread_id", nullable = false)
    val thread = TValue.TObjectReference.TThreadReference[e.thread()]

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "catch_location_id", nullable = true)
    val catchLocation = e.catchLocation()?.let { TLocation[it] }

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "location_id", nullable = false)
    val location = TLocation[e.location()]

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "exception_obj_ref_id", nullable = false)
    val exception = TValue.TObjectReference[e.exception()]
}