package com.rapidops.salesmatechatsdk.app.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rapidops.salesmatechatsdk.app.activity.main.MainViewModel
import com.rapidops.salesmatechatsdk.app.di.ApplicationScope
import com.rapidops.salesmatechatsdk.app.di.ViewModelFactory
import com.rapidops.salesmatechatsdk.app.di.ViewModelKey
import com.rapidops.salesmatechatsdk.app.fragment.conversation_list.ConversationListViewModel
import com.rapidops.salesmatechatsdk.app.fragment.recent_list.RecentChatViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
internal class ViewModelModule {

    @Provides
    @ApplicationScope
    internal fun viewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory {
        return viewModelFactory
    }


    @Provides
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal fun mainViewModel(mainViewModel: MainViewModel): ViewModel {
        return mainViewModel
    }

    @Provides
    @IntoMap
    @ViewModelKey(RecentChatViewModel::class)
    internal fun recentChatViewModel(recentChatViewModel: RecentChatViewModel): ViewModel {
        return recentChatViewModel
    }

    @Provides
    @IntoMap
    @ViewModelKey(ConversationListViewModel::class)
    internal fun conversationListViewModel(conversationListViewModel: ConversationListViewModel): ViewModel {
        return conversationListViewModel
    }

    /*@Binds
    @IntoMap
    @ViewModelKey(ConversationListViewModel::class)
    internal abstract fun recentChatViewModel(viewModel: ConversationListViewModel): ViewModel*/

}