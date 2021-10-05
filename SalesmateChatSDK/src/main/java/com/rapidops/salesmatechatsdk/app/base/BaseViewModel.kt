package com.rapidops.salesmatechatsdk.app.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rapidops.salesmatechatsdk.app.coroutines.ICoroutineContextProvider
import com.rapidops.salesmatechatsdk.domain.exception.SalesmateChatException
import kotlinx.coroutines.*

internal open class BaseViewModel(private val dispatcher: ICoroutineContextProvider) : ViewModel() {
    private var scope = CoroutineScope(Job() + Dispatchers.Main)
    val progress = MutableLiveData<Boolean>()
    val dataProgress = MutableLiveData<Boolean>()
    val salesMateChatException = MutableLiveData<SalesmateChatException>()

    var cancellableScope = CoroutineScope(Job() + Dispatchers.Main)

    fun withoutProgress(
        async: suspend () -> Unit,
        errorLogic: (throwable: Exception) -> Unit = { defaultErrorHandler(it) }
    ) {
        scope.launch(dispatcher.io) {
            try {
                async()
            } catch (ex: Exception) {
                withContext(dispatcher.ui) {
                    errorLogic(ex)
                }
            }
        }
    }

    protected fun defaultErrorHandler(exception: Exception) {
        salesMateChatException.value = if (exception is SalesmateChatException) exception
        else SalesmateChatException(SalesmateChatException.Kind.UNEXPECTED)
    }


    fun withProgress(
        async: suspend () -> Unit,
        errorLogic: (throwable: Exception) -> Unit = { defaultErrorHandler(it) }
    ) {

        scope.launch(dispatcher.io) {
            try {
                withContext(dispatcher.ui) {
                    progress.value = true
                }
                async()
            } catch (ex: Exception) {
                withContext(dispatcher.ui) {
                    errorLogic(ex)
                }
            } finally {
                withContext(dispatcher.ui) {
                    progress.value = false
                }
            }
        }
    }

    fun cancelableJobWithProgress(
        async: suspend () -> Unit,
        errorLogic: (throwable: Exception) -> Unit = {
            defaultErrorHandler(it)
        }
    ) {
        cancellableScope = CoroutineScope(Job() + Dispatchers.Main)
        cancellableScope.launch(dispatcher.io) {
            try {
                delay(2000L)
                withContext(dispatcher.ui) {
                    progress.value = true
                }
                async()
            } catch (ex: Exception) {
                withContext(dispatcher.ui) {
                    errorLogic(ex)
                }
            } finally {
                withContext(dispatcher.ui) {
                    progress.value = false
                }
            }
        }
    }
}