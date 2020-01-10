package ru.c57m7a.dbr

import kotlinx.coroutines.CommonPool
import kotlinx.coroutines.IO
import kotlin.coroutines.CoroutineContext

internal val telegramCoroutineContext: CoroutineContext = CommonPool
internal val serverCoroutineContext: CoroutineContext = CommonPool
internal val backendCoroutineContext: CoroutineContext = IO