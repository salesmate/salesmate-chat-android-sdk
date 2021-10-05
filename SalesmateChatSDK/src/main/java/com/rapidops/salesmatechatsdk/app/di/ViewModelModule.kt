package com.rapidops.salesmatechatsdk.app.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rapidops.salesmatechatsdk.app.activity.main.MainViewModel
import com.rapidops.salesmatechatsdk.app.fragment.conversation_list.ConversationListViewModel
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
    @ViewModelKey(ConversationListViewModel::class)
    internal fun conversationListViewModel(conversationListViewModel: ConversationListViewModel): ViewModel {
        return conversationListViewModel
    }

    /*@Binds
    @IntoMap
    @ViewModelKey(ConversationListViewModel::class)
    internal abstract fun conversationListViewModel(viewModel: ConversationListViewModel): ViewModel*/

}