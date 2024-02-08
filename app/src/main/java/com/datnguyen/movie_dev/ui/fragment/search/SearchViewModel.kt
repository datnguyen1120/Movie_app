package com.datnguyen.movie_dev.ui.fragment.search

import androidx.lifecycle.MutableLiveData
import com.datnguyen.movie_dev.app_model.Cast
import com.datnguyen.movie_dev.app_model.Movie
import com.datnguyen.movie_dev.extras.LiveEvent
import com.datnguyen.movie_dev.services.api_repository.MovieRepository
import com.datnguyen.movie_dev.services.api_service.MovieService
import com.datnguyen.movie_dev.ui.adapter.search.SearchAdapter
import com.datnguyen.movie_dev.ui.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel : BaseViewModel() {
    var movies: MutableList<Movie?> = arrayListOf()
    var movieRepository: MovieRepository
    var searchText: String? = null

    var showEmpty: LiveEvent<Boolean> = LiveEvent()
    var itemClickEvent = LiveEvent<Movie?>()

    //adapter to display movies list
    var adapter: SearchAdapter

    init {
        val movieService = MovieService.getInstance()
        movieRepository = MovieRepository(movieService)

        //init adapter
        adapter = SearchAdapter(movies)
        adapter.itemClickEvent = itemClickEvent
    }

    fun searchMovie() {
        searchMovie(searchText, false)
    }

    //displayLoading - to display progress loading in case user clicks submit button
    fun searchMovie(searchText: String?, displayLoading: Boolean) {
        if (displayLoading) {
            loading.postValue(true)
        }

        this.searchText = searchText
        if (searchText?.isNotEmpty() == true) {
            job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                val response = movieRepository.searchMovies(searchText)
                withContext(Dispatchers.Main) {
                    if (response.success != false) {
                        addMovies(response.results)
                    } else {
                        onError(response.statusMessage)
                    }
                    loading.postValue(false)
                }
            }
        } else {
            movies = arrayListOf()
        }
    }

    private fun addMovies(movies: List<Movie?>?) {
        this.movies.clear()
        if (movies?.isNotEmpty() == true) {
            this.movies.addAll(movies)

            showEmpty.postValue(false)
        } else {
            showEmpty.postValue(true)
        }

        adapter.notifyDataSetChanged()
    }
}