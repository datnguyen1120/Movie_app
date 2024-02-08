package com.datnguyen.movie_dev.ui.fragment.favorites

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.datnguyen.movie_dev.MyApplication
import com.datnguyen.movie_dev.app_model.Movie
import com.datnguyen.movie_dev.extras.LiveEvent
import com.datnguyen.movie_dev.services.api_repository.FavoriteRepository
import com.datnguyen.movie_dev.services.api_service.FavoriteService
import com.datnguyen.movie_dev.ui.adapter.favorite.FavoriteAdapter
import com.datnguyen.movie_dev.ui.adapter.favorite.FavoriteVH
import com.datnguyen.movie_dev.ui.base.BaseViewWithAdapterModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesViewModel(lifecycleOwner: LifecycleOwner) : BaseViewWithAdapterModel<Movie, FavoriteVH>(lifecycleOwner) {
    var favoriteRepository: FavoriteRepository
    var itemClickEvent = LiveEvent<Movie?>()
    var showRefreshEvent = LiveEvent<Boolean>()
    var showEmptyEvent = LiveEvent<Boolean>()

    init {
        val favoriteService = FavoriteService.getInstance()
        favoriteRepository = FavoriteRepository(favoriteService)

        initData()
    }

    override fun initData() {
        //init adapter
        adapter = FavoriteAdapter(items)
        (adapter as FavoriteAdapter).itemClickEvent = itemClickEvent

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

            val accountId = MyApplication.instance().session?.accountId
            accountId?.let {
                val response = favoriteRepository.getFavoriteMovies(accountId, page)

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
                    //hide swipe refresh
                    showRefreshEvent.postValue(false)

                    //reset flag value
                    isLoading = false
                }
            }
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        //display swipe refresh
        showRefreshEvent.postValue(true)
    }
}