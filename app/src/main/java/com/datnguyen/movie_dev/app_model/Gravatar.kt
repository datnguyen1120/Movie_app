package com.datnguyen.movie_dev.app_model


import com.datnguyen.movie_dev.app_model.BaseModel
import com.google.gson.annotations.SerializedName

data class Gravatar(
    @SerializedName("hash")
    var hash: String?
) : BaseModel()