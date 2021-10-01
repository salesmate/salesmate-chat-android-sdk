package com.rapidops.salesmatechatsdk.app.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rapidops.salesmatechatsdk.core.SalesmateChat


internal fun <T : ViewModel> AppCompatActivity.obtainViewModel(viewModelClass: Class<T>) =
    ViewModelProvider(this, SalesmateChat.daggerDataComponent.getViewModelFactory()).get(
        viewModelClass
    )

internal fun <T : ViewModel> Fragment.obtainViewModel(viewModelClass: Class<T>) =
    ViewModelProvider(this, SalesmateChat.daggerDataComponent.getViewModelFactory()).get(
        viewModelClass
    )
