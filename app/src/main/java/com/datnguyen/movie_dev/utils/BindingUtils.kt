package com.datnguyen.movie_dev.utils

import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * class have functions to binding the layout faster
 */
object BindingUtils {
    @JvmStatic
    @BindingAdapter("linearManager")
    fun linearManager(recyclerView: RecyclerView, _int: Int) {
        when (_int) {
            0     //0 - horizontal
            -> recyclerView.layoutManager = LinearLayoutManager(
                recyclerView.context,
                LinearLayoutManager.HORIZONTAL, false
            )
            1     //1 - vertical
            -> recyclerView.layoutManager = LinearLayoutManager(
                recyclerView.context,
                LinearLayoutManager.VERTICAL, false
            )
            2     //2 - no scroll vertical
            -> recyclerView.layoutManager = object : LinearLayoutManager(recyclerView.context) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            3     //3 - no scroll horizontal
            -> recyclerView.layoutManager = object : LinearLayoutManager(recyclerView.context) {
                override fun canScrollHorizontally(): Boolean {
                    return false
                }
            }
            else -> {
            }
        }
    }

    @JvmStatic
    @BindingAdapter("gridManager", "rows")
    fun gridManager(recyclerView: RecyclerView, _int: Int, spanCount: Int) {
        when (_int) {
            0     //0 - grid layout
            -> recyclerView.layoutManager = GridLayoutManager(recyclerView.context, spanCount)
            else -> {
            }
        }
    }
}
