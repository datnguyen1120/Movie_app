package com.datnguyen.movie_dev.ui.fragment.home

import androidx.lifecycle.LiveData
import com.datnguyen.movie_dev.extras.LiveEvent
import com.datnguyen.movie_dev.ui.base.BaseViewModel

class HomeViewModel : BaseViewModel() {

    private val _text = LiveEvent<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}