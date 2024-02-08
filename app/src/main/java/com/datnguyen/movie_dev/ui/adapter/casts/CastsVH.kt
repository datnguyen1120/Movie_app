package com.datnguyen.movie_dev.ui.adapter.casts

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.datnguyen.movie_dev.R

class CastsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var parent = itemView.findViewById<View>(R.id.parent)
    var tvName = itemView.findViewById<TextView>(R.id.tv_name)
    var imv = itemView.findViewById<ImageView>(R.id.imv)
}