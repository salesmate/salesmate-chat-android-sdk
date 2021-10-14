package com.rapidops.salesmatechatsdk.app.fragment.recent_list

import com.rapidops.salesmatechatsdk.app.base.BaseViewModel
import com.rapidops.salesmatechatsdk.app.coroutines.ICoroutineContextProvider
import com.rapidops.salesmatechatsdk.app.utils.AppEvent
import com.rapidops.salesmatechatsdk.app.utils.EventBus
import com.rapidops.salesmatechatsdk.app.utils.SingleLiveEvent
import com.rapidops.salesmatechatsdk.data.resmodels.PingRes
import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.models.ConversationDetailItem
import com.rapidops.salesmatechatsdk.domain.usecases.GetConversationListUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

internal class RecentChatViewModel @Inject constructor(
    private val appSettingsDataSource: IAppSettingsDataSource,
    private val coroutineContextProvider: ICoroutineContextProvider,
    private val getConversationUseCase: GetConversationListUseCase
) : BaseViewModel(coroutineContextProvider) {

    val recentViewProgress = SingleLiveEvent<Boolean>()
    val showConversationList = SingleLiveEvent<List<ConversationDetailItem>>()

    fun subscribe(showProgress: Boolean = true) {
        if (showProgress) {
            recentViewProgress.value = true
        }
        withoutProgress({
            val conversationList =
                getConversationUseCase.execute(GetConversationListUseCase.Param(3, 0))
            withContext(coroutineContextProvider.ui) {
                showConversationList.value = conversationList
                if (showProgress) {
                    recentViewProgress.value = false
                }
            }
        }, {
            if (showProgress) {
                recentViewProgress.value = false
            }
            defaultErrorHandler(it)
        })

        subscribeEvents()
    }

    private fun subscribeEvents() {
        subscribeEvent {
            EventBus.events.filterIsInstance<AppEvent.UpdateConversationDetailEvent>()
                .collectLatest { updateConversationDetail ->
                    val conversationDetailItem = updateConversationDetail.data
                    val list = showConversationList.value?.toMutableList() ?: mutableListOf()
                    val indexOfFirst =
                        list.indexOfFirst { it.conversations?.id == conversationDetailItem.conversations?.id }
                    if (indexOfFirst != -1) {
                        list[indexOfFirst] = conversationDetailItem
                        Collections.swap(list, indexOfFirst, 0)
                    } else {
                        list.add(0, conversationDetailItem)
                    }
                    showConversationList.value = list
                }
        }
    }


    val pingRes: PingRes by lazy {
        appSettingsDataSource.pingRes
    }

}