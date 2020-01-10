package ru.c57m7a.dbr.db

import javax.persistence.*

@Entity
@Table
class VMProfile(
    @Column
    val url: String
) {
    @Id
    @GeneratedValue
    val id = 0
}