package com.datnguyen.movie_dev.services.model

import com.datnguyen.movie_dev.app_model.Cast
import com.datnguyen.movie_dev.app_model.Crew
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CreditsResponse : ErrorModel() {
    @SerializedName("cast")
    @Expose
    var cast: List<Cast>? = null

    @SerializedName("crew")
    @Expose
    var crew: List<Crew>? = null
}
