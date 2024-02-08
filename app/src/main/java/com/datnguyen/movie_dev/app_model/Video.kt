package com.datnguyen.movie_dev.app_model


import com.datnguyen.movie_dev.BuildConfig
import com.google.gson.annotations.SerializedName

data class Video(
    @SerializedName("id")
    var id: String?,
    @SerializedName("iso_3166_1")
    var iso31661: String?,
    @SerializedName("iso_639_1")
    var iso6391: String?,
    @SerializedName("key")
    var key: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("official")
    var official: Boolean?,
    @SerializedName("published_at")
    var publishedAt: String?,
    @SerializedName("site")     //YouTube - Vimeo
    var site: String?,
    @SerializedName("size")
    var size: Int?,
    @SerializedName("type")
    var type: String?
) {
    fun isTrailer(): Boolean {
        if (type.equals("Trailer", true) && site.equals("Youtube", true)) {
            return true
        }
        return false
    }

    fun getVideoUrl(): String? {
        if (key != null) {
            return when (site) {
                "Vimeo" -> BuildConfig.BaseVimeoUrl + key
                else -> BuildConfig.BaseYoutubeUrl + key
            }
        }
        return null
    }
}