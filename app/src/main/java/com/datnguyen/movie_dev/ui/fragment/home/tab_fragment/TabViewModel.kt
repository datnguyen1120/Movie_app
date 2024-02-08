package com.datnguyen.movie_dev.ui.fragment.home.tab_fragment

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.datnguyen.movie_dev.app_model.Movie
import com.datnguyen.movie_dev.extras.LiveEvent
import com.datnguyen.movie_dev.extras.enums.HomeType
import com.datnguyen.movie_dev.services.api_repository.MovieRepository
import com.datnguyen.movie_dev.services.api_service.MovieService
import com.datnguyen.movie_dev.ui.adapter.home_movies.HomeMoviesAdapter
import com.datnguyen.movie_dev.ui.adapter.home_movies.HomeMoviesVH
import com.datnguyen.movie_dev.ui.base.BaseViewWithAdapterModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TabViewModel(val homeType: HomeType, lifecycleOwner: LifecycleOwner) : BaseViewWithAdapterModel<Movie, HomeMoviesVH>(lifecycleOwner) {
    var movieRepository: MovieRepository
    var itemClickEvent = LiveEvent<Movie>()
    var showRefreshEvent = LiveEvent<Boolean>()
    var showEmptyEvent = LiveEvent<Boolean>()

    init {
        val movieService = MovieService.getInstance()
        movieRepository = MovieRepository(movieService)

        initData()
    }

    override fun initData() {
        //init adapter
        adapter = HomeMoviesAdapter(items)
        (adapter as HomeMoviesAdapter).itemClickEvent = itemClickEvent

        //display loading for initialization
        loading.postValue(true)
        //load data
        loadData()

       super.initData()
    }

    override fun loadData() {
        super.loadData()
        //set flag for loading
        isLoading = true
        //call api
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            //load the movies follow the home type
            val response = when (homeType) {
                HomeType.POPULAR -> movieRepository.getPopular(page)
                HomeType.NOW_PLAYING -> movieRepository.getNowPlaying(page)
                HomeType.UPCOMING -> movieRepository.getUpcoming(page)
                HomeType.TOP_RATED -> movieRepository.getTopRated(page)
            }

            //handle response
            withContext(Dispatchers.Main) {
                if (response.success != false) {
                    addItems(response.results)

                    //check can load more
                    canLoadMore = (response.totalPages ?: 0) > page

                    //show empty layout
                    showEmptyEvent.postValue(items.isEmpty())
                } else {
                    onError(response.statusMessage)
                }
                //hide loading
                loading.postValue(false)

                //hide refresh
                showRefreshEvent.postValue(false)

                //reset flag value
                isLoading = false
            }
        }
    }
}