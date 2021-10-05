package com.rapidops.salesmatechatsdk.app.fragment.conversation_list

import com.rapidops.salesmatechatsdk.app.base.BaseViewModel
import com.rapidops.salesmatechatsdk.app.coroutines.ICoroutineContextProvider
import javax.inject.Inject

internal class ConversationListViewModel @Inject constructor(
    private val coroutineContextProvider: ICoroutineContextProvider,
) : BaseViewModel(coroutineContextProvider) {


    fun subscribe() {

    }

}