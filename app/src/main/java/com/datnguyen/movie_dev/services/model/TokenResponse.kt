package com.datnguyen.movie_dev.services.model


import com.google.gson.annotations.SerializedName

class TokenResponse : ErrorModel() {
    @SerializedName("expires_at")
    var expiresAt: String? = null

    @SerializedName("request_token")
    var requestToken: String? = null
}