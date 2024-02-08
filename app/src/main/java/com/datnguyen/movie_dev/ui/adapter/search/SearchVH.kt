package com.datnguyen.movie_dev.ui.adapter.search

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.datnguyen.movie_dev.R

class SearchVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var parent = itemView.findViewById<View>(R.id.parent)
    var imv = itemView.findViewById<ImageView>(R.id.imv)
    var tvTitle = itemView.findViewById<TextView>(R.id.tv_title)
    var tvRateScore = itemView.findViewById<TextView>(R.id.tv_rate_score)
    var tvDesc = itemView.findViewById<TextView>(R.id.tv_desc)
    var tvDate = itemView.findViewById<TextView>(R.id.tv_date)
}