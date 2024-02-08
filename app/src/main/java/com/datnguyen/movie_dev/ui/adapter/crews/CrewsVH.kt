package com.datnguyen.movie_dev.ui.adapter.crews

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.datnguyen.movie_dev.R

class CrewsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var parent = itemView.findViewById<View>(R.id.parent)
    var tvName = itemView.findViewById<TextView>(R.id.tv_name)
    var tvJob = itemView.findViewById<TextView>(R.id.tv_job)
}