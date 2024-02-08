package com.datnguyen.movie_dev.ui.adapter.casts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.datnguyen.movie_dev.R
import com.datnguyen.movie_dev.app_model.Cast
import com.datnguyen.movie_dev.extras.LiveEvent
import com.datnguyen.movie_dev.utils.Utils


class CastsAdapter(private val casts: MutableList<Cast?>) : RecyclerView.Adapter<CastsVH>() {
    var itemWidth: Int
    var itemClickEvent = LiveEvent<Cast?>()

    init {
        val screenWidth = Utils.getScreenWidth()
        val paddingSize: Int = Utils.convertDPtoPixel(16 * 2)    //padding
        itemWidth = (screenWidth - paddingSize) / 2  //item width
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastsVH {
        val viewLayout =
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_cast,
                parent, false
            )
        return CastsVH(viewLayout)
    }

    override fun onBindViewHolder(holder: CastsVH, position: Int) {
        val cast = casts[position]
        holder.tvName.text = cast?.name

        //set image
        cast?.profilePath?.let {
            Glide.with(holder.imv.context)
                .load(Utils.getImageUrl(cast?.profilePath))
                .override(itemWidth, itemWidth)
                .circleCrop()
                .into(holder.imv)
        } ?: kotlin.run {
            holder.imv.setImageResource(R.drawable.ic_avatar)
        }

        //set event click
        holder.parent.setOnClickListener {
            itemClickEvent.postValue(cast)
        }

        //set item width
        holder.parent.layoutParams.width = itemWidth
    }

    override fun getItemCount(): Int {
        return casts.size
    }
}