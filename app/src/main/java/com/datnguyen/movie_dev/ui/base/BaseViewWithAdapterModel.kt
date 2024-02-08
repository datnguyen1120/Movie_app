package com.datnguyen.movie_dev.ui.base

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView

/**
 * X is Model
 * Y is View Holder
 */
abstract class BaseViewWithAdapterModel<X, Y : RecyclerView.ViewHolder>(private val lifecycleOwner: LifecycleOwner) : BaseViewModel() {
    /**
     * Base settings for recyclerview
     */
    //list item
    var items: MutableList<X?> = arrayListOf()

    // load paging
    var page: Int = 1   //first page = 1
    var canLoadMore: Boolean = true //can load more or not
    var isRefreshing = false    //is loading refresh
    var isLoading = false   //is loading

    //adapter
    lateinit var adapter: BaseAdapter<X, Y>


    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

   open fun initData(){
       //load more
       adapter.loadMoreAction.observe(lifecycleOwner) {
           onLoadMore()
       }
    }

    open fun loadData() {
        adapter.isLoading = true
    }

    //if it if load data in background.
    protected fun addItems(items: List<X>?) {
        if (isRefreshing) {
            isRefreshing = false
            this.items.clear()
        } else {
            adapter.displayLoadMoreProcess(false)
        }

        items?.let {
            this.items.addAll(items)
        }

        adapter.isLoading = false
        adapter.notifyDataSetChanged()
    }

    open fun onRefresh() {
        //reset page
        page = 1
        //set flag refresh
        isRefreshing = true
        //load data
        loadData()
    }

    private fun onLoadMore() {
        if ((canLoadMore) && !isLoading) {
            //increase page
            page += 1
            //display load more progress
            adapter.displayLoadMoreProcess(true)
            //load data
            loadData()
        }
    }
}