package com.datnguyen.movie_dev.ui.adapter.home_movies

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.datnguyen.movie_dev.R

class HomeMoviesVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var imv = itemView.findViewById<ImageView>(R.id.imv)
    var parent = itemView.findViewById<View>(R.id.parent)
}