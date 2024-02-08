package com.datnguyen.movie_dev.services.model


import com.google.gson.annotations.SerializedName

data class FavoriteMovieRequest(
    @SerializedName("favorite")
    var favorite: Boolean?,
    @SerializedName("media_id")
    var mediaId: Int?,
    @SerializedName("media_type")
    var mediaType: String?
)