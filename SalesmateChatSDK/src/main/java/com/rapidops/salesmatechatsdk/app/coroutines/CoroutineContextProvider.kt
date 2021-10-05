package com.rapidops.salesmatechatsdk.app.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal class CoroutineContextProvider : ICoroutineContextProvider {
    override val io: CoroutineDispatcher
        get() = Dispatchers.IO
    override val ui: CoroutineDispatcher
        get() = Dispatchers.Main
    override val common: CoroutineDispatcher
        get() = Dispatchers.Default
}