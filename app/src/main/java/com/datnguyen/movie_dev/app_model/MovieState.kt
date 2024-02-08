package com.datnguyen.movie_dev.app_model


import com.google.gson.annotations.SerializedName

data class MovieState(
    @SerializedName("favorite")
    var favorite: Boolean?,
    @SerializedName("id")
    var id: Int?,
): BaseModel()