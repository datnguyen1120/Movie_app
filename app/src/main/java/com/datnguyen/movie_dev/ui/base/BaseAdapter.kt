package com.datnguyen.movie_dev.ui.base

import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.RecyclerView
import com.datnguyen.movie_dev.extras.LiveEvent

abstract class BaseAdapter<X, Y : RecyclerView.ViewHolder>(var items: MutableList<X?>) : RecyclerView.Adapter<Y>() {
    //the items else in the waiting visible threshold
    protected val VISIBLE_THRESHOLD = 1

    //item type
    protected val VIEW_PROG = 0
    protected val VIEW_ITEM = 1

    var isLoading = false   //flag to know is loading
    var loadMoreAction = LiveEvent<Int>() //action to trigger load more data

    override fun onBindViewHolder(holder: Y, position: Int) {
        if (getItemViewType(position) != VIEW_PROG) {
            if (position == itemCount - VISIBLE_THRESHOLD && !isLoading) {
                loadMoreAction.postValue(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun displayLoadMoreProcess(isProgress: Boolean) {
        if (items.size > 0) {
            if (isProgress) {
                Handler(Looper.getMainLooper()).post {
                    items.add(null)     //null is progress layout on bottom list
                    notifyItemInserted(items.size - 1)
                }
            } else {
                items.removeAt(items.size - 1)
                notifyItemRemoved(items.size)
                notifyItemRangeChanged(items.size, itemCount)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] == null) {
            VIEW_PROG
        } else
            VIEW_ITEM
    }
}