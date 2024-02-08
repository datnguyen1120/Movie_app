package com.datnguyen.movie_dev.app_model


import com.google.gson.annotations.SerializedName

data class Rated(
    @SerializedName("value")
    var value: Int?
)