package com.datnguyen.movie_dev.app_model


import com.google.gson.annotations.SerializedName

data class TMDB(
    @SerializedName("avatar_path")
    var avatarPath: String?
) : BaseModel()