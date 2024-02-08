package com.datnguyen.movie_dev.ui.adapter.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.datnguyen.movie_dev.R
import com.datnguyen.movie_dev.app_model.Cast
import com.datnguyen.movie_dev.app_model.Movie
import com.datnguyen.movie_dev.extras.LiveEvent
import com.datnguyen.movie_dev.utils.Utils


class SearchAdapter(private val movies: List<Movie?>) : RecyclerView.Adapter<SearchVH>() {
    var itemClickEvent = LiveEvent<Movie?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchVH {
        val viewLayout = LayoutInflater.from(parent.context).inflate(
            R.layout.item_search_movie,
            parent, false
        )
        return SearchVH(viewLayout)
    }

    override fun onBindViewHolder(holder: SearchVH, position: Int) {
        val movie = movies[position]
        movie?.let {
            holder.tvTitle.text = movie.title
            holder.tvRateScore.text = movie.voteAverage?.toString()
            holder.tvDesc.text = movie.overview
            holder.tvDate.text = movie.releaseDate

            //set image
            Glide.with(holder.imv.context)
                .load(Utils.getImageUrl(movie.posterPath))
                .override(Utils.convertDPtoPixel(95), Utils.convertDPtoPixel(120))
                .centerCrop()
//                .transform(GranularRoundedCorners(16f, 16f, 16f, 16f))
                .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))      //round corner
//                .apply(RequestOptions().transform(RoundedCornersTransformation (context, 16, 0)))
                .into(holder.imv)

            //set item click event
            holder.parent.setOnClickListener {
                itemClickEvent.postValue(movie)
            }
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

}