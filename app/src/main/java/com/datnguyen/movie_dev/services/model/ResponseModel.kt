package com.datnguyen.movie_dev.services.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseModel<T> : ErrorModel() {
    var data = this

    //for list response
    @SerializedName("results")
    @Expose
    var results: T? = null

    @SerializedName("page")
    @Expose
    var page: Int? = null

    @SerializedName("total_pages")
    @Expose
    var totalPages: Int? = null
}
