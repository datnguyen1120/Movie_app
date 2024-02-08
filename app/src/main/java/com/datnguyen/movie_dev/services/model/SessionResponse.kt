package com.datnguyen.movie_dev.services.model


import com.google.gson.annotations.SerializedName

class SessionResponse : ErrorModel() {
    @SerializedName("session_id")
    var sessionId: String? = null
}