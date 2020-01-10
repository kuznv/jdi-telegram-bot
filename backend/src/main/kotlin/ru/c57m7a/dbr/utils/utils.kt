package ru.c57m7a.dbr.utils

inline fun <T, reified E : Exception> tryOrNull(block: () -> T): T? =
    try {
        block()
    } catch (e: Exception) {
        if (e !is E)
            throw e
        null
    }