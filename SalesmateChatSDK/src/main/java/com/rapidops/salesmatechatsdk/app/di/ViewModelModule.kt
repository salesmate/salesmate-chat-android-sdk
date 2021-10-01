package com.rapidops.salesmatechatsdk.app.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rapidops.salesmatechatsdk.app.activity.main.MainViewModel
import com.rapidops.salesmatechatsdk.app.fragment.HomeViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class ViewModelModule {

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
    @ViewModelKey(HomeViewModel::class)
    internal fun homeViewModel(homeViewModel: HomeViewModel): ViewModel {
        return homeViewModel
    }
}