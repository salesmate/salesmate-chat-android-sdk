package com.rapidops.salesmatechatsdk.app.fragment

import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.app.base.BaseViewModel
import com.rapidops.salesmatechatsdk.app.coroutines.ICoroutineContextProvider
import com.rapidops.salesmatechatsdk.app.utils.SingleLiveEvent
import com.rapidops.salesmatechatsdk.domain.usecases.GetDataUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val coroutineContextProvider: ICoroutineContextProvider,
    private val getDataUseCase: GetDataUseCase
) : BaseViewModel(coroutineContextProvider) {

    val dataLive = SingleLiveEvent<JsonElement>()

    fun subscribe() {
        withProgress({
            val data = getDataUseCase.execute("")
            withContext(coroutineContextProvider.ui) {
                dataLive.value = data
            }
        }, {
            defaultErrorHandler(it)
        })

    }

}