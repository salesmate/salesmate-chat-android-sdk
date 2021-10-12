package com.rapidops.salesmatechatsdk.app.fragment.recent_list

import com.rapidops.salesmatechatsdk.app.base.BaseViewModel
import com.rapidops.salesmatechatsdk.app.coroutines.ICoroutineContextProvider
import com.rapidops.salesmatechatsdk.app.utils.AppEvent
import com.rapidops.salesmatechatsdk.app.utils.EventBus
import com.rapidops.salesmatechatsdk.app.utils.SingleLiveEvent
import com.rapidops.salesmatechatsdk.data.resmodels.PingRes
import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.models.ConversationDetailItem
import com.rapidops.salesmatechatsdk.domain.usecases.GetConversationDetailUseCase
import com.rapidops.salesmatechatsdk.domain.usecases.GetConversationUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class RecentChatViewModel @Inject constructor(
    private val appSettingsDataSource: IAppSettingsDataSource,
    private val coroutineContextProvider: ICoroutineContextProvider,
    private val getConversationUseCase: GetConversationUseCase,
    private val getConversationDetailUseCase: GetConversationDetailUseCase,
) : BaseViewModel(coroutineContextProvider) {

    val recentViewProgress = SingleLiveEvent<Boolean>()
    val showConversationList = SingleLiveEvent<List<ConversationDetailItem>>()

    fun subscribe(showProgress: Boolean = true) {
        if (showProgress) {
            recentViewProgress.value = true
        }
        withoutProgress({
            val conversationList =
                getConversationUseCase.execute(GetConversationUseCase.Param(3, 0))
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

        subscribeEvent {
            EventBus.events.filterIsInstance<AppEvent.NewMessageEvent>().collectLatest { event ->
                val find = showConversationList.value?.find {
                    it.conversations?.id == event.data.conversationId
                }
                find?.let {
                    loadConversationDetail(it.conversations?.id!!)
                } ?: run {
                    subscribe(false)
                }
            }
        }
    }

    private fun loadConversationDetail(conversationId: String) {
        withoutProgress({
            val conversationDetailItem = getConversationDetailUseCase.execute(
                GetConversationDetailUseCase.Param(conversationId)
            )
            val list = showConversationList.value?.toMutableList() ?: mutableListOf()
            list.removeAll { it.conversations?.id == conversationId }
            list.add(0, conversationDetailItem)
            withContext(coroutineContextProvider.ui) {
                showConversationList.value = list
            }

        })
    }


    val pingRes: PingRes by lazy {
        appSettingsDataSource.pingRes
    }

}