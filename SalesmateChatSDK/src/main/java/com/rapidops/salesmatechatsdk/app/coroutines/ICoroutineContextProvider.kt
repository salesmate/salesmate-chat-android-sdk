package com.rapidops.salesmatechatsdk.app.coroutines

import kotlin.coroutines.CoroutineContext

interface ICoroutineContextProvider {
    val io: CoroutineContext
    val ui: CoroutineContext
    val common: CoroutineContext
}