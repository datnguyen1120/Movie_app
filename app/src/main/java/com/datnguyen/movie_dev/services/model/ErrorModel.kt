package com.datnguyen.movie_dev.services.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
open class ErrorModel : Parcelable {
    @SerializedName("status_code")
    @Expose
    var statusCode: Int? = null

    @SerializedName("status_message")
    @Expose
    var statusMessage: String? = null

    @SerializedName("success")
    @Expose
    var success: Boolean? = true
}
