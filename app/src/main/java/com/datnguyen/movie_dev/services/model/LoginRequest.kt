package com.datnguyen.movie_dev.services.model


import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("username")
    var username: String?,
    @SerializedName("password")
    var password: String?,
    @SerializedName("request_token")
    var requestToken: String?
)