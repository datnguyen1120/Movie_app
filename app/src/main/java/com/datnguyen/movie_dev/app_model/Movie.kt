package com.datnguyen.movie_dev.app_model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val id: Int,
    @Expose
    @SerializedName("homePage")
    val homePage: String?,
    @Expose
    @SerializedName("genres")
    val genres: List<Genre>?,
    @Expose
    @SerializedName("poster_path")
    val posterPath: String?,
    @Expose
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @Expose
    @SerializedName("title")
    val title: String?,
    @Expose
    @SerializedName("status")
    val status: String?,
    @Expose
    @SerializedName("vote_average")
    val voteAverage: Double?,
    @Expose
    @SerializedName("vote_count")
    val voteCount: Long?,
    @Expose
    @SerializedName("release_date")
    val releaseDate: String?,
    @Expose
    @SerializedName("overview")
    val overview: String?,
    @Expose
    @SerializedName("revenue")
    val revenue: Long?,

    //local value
    var isMarkedFavorite: Boolean = false
) : BaseModel()
