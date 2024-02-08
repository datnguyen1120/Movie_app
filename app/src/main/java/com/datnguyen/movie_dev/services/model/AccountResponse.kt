package com.datnguyen.movie_dev.services.model


import com.datnguyen.movie_dev.app_model.Avatar
import com.datnguyen.movie_dev.utils.Utils
import com.google.gson.annotations.SerializedName

data class AccountResponse(
    @SerializedName("avatar")
    var avatar: Avatar?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("username")
    var username: String?
) : ErrorModel() {
    fun getAvatarUrl(): String {
        //return avatar path if it exists
        avatar?.tmdb?.avatarPath?.let {
            return Utils.getImageUrl(it)!!
        }

        //return gravatar
        //https://secure.gravatar.com/avatar/c9e9fc152ee756a900db85757c29815d.jpg?s=200
        return "https://secure.gravatar.com/avatar/${avatar?.gravatar?.hash}.jpg?s=200"
    }
}