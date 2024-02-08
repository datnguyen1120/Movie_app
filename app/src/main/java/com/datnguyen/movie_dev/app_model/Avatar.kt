package com.datnguyen.movie_dev.app_model


import com.google.gson.annotations.SerializedName

data class Avatar(
    @SerializedName("gravatar")
    var gravatar: Gravatar?,
    @SerializedName("tmdb")
    var tmdb: TMDB?
): BaseModel()