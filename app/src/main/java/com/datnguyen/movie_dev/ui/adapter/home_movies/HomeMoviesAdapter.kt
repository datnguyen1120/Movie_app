package com.datnguyen.movie_dev.ui.adapter.home_movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.datnguyen.movie_dev.R
import com.datnguyen.movie_dev.app_model.Movie
import com.datnguyen.movie_dev.extras.LiveEvent
import com.datnguyen.movie_dev.ui.base.BaseAdapter
import com.datnguyen.movie_dev.utils.Utils


class HomeMoviesAdapter(private val movies: MutableList<Movie?>) : BaseAdapter<Movie, HomeMoviesVH>(movies) {
    var itemWidth: Int
    var itemHeight: Int
    var itemClickEvent = LiveEvent<Movie>()

    init {
        val screenWidth = Utils.getScreenWidth()
        val paddingSize: Int = Utils.convertDPtoPixel(14 * 3)    //padding between 3 rows
        itemWidth = (screenWidth - paddingSize) / 3  //item width
        itemHeight = itemWidth * 145 / 100    //the ratio from figma design
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeMoviesVH {
        val viewLayout = when (viewType) {
            VIEW_PROG -> {
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_process_loading,
                    parent, false
                )
            }
            else -> {
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_home_movie,
                    parent, false
                )
            }
        }
        return HomeMoviesVH(viewLayout)
    }

    override fun onBindViewHolder(holder: HomeMoviesVH, position: Int) {
        super.onBindViewHolder(holder, position)

        if (getItemViewType(position) != VIEW_PROG) {
            val movie = movies[position]

            //set item width
            holder.imv.layoutParams.width = itemWidth
            holder.imv.layoutParams.height = itemHeight

            //load image view
            movie?.let { movie ->
                //set image
                Glide.with(holder.imv.context)
                    .load(Utils.getImageUrl(movie.posterPath))
                    .override(itemWidth, itemHeight)
                    .centerCrop()
//                .transform(GranularRoundedCorners(16f, 16f, 16f, 16f))
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))      //round corner
//                .apply(RequestOptions().transform(RoundedCornersTransformation (context, 16, 0)))
                    .into(holder.imv)

                //set event click
                holder.parent.setOnClickListener {
                    itemClickEvent.postValue(movie)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}