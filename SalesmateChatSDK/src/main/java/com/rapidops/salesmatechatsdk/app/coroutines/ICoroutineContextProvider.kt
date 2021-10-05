package com.rapidops.salesmatechatsdk.app.coroutines

import kotlin.coroutines.CoroutineContext

internal interface ICoroutineContextProvider {
    val io: CoroutineContext
    val ui: CoroutineContext
    val common: CoroutineContext
}