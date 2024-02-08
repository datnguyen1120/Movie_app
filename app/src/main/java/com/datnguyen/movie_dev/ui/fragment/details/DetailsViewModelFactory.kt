package com.datnguyen.movie_dev.ui.fragment.details

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.datnguyen.movie_dev.app_model.Movie
import com.datnguyen.movie_dev.extras.enums.HomeType

class DetailsViewModelFactory(val movie: Movie?): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailsViewModel(movie) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}