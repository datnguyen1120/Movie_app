package com.datnguyen.movie_dev.services.model


import com.google.gson.annotations.SerializedName

data class DeleteSessionRequest(
    @SerializedName("session_id")
    var sessionId: String?
)