package com.datnguyen.movie_dev.ui.fragment.home.tab_fragment

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.datnguyen.movie_dev.extras.enums.HomeType

class TabViewModelFactory(val homeType: HomeType, val lifecycleOwner: LifecycleOwner): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TabViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TabViewModel(homeType, lifecycleOwner) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}