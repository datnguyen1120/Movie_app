package com.datnguyen.movie_dev.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.startActivity
import com.datnguyen.movie_dev.BuildConfig
import com.datnguyen.movie_dev.MyApplication
import com.datnguyen.movie_dev.app_model.Video
import java.io.Serializable


object Utils {
    //because the getSerializable is deprecated on android >=33. So we need to have 2 options
    fun <T : Serializable?> getSerializable(bundle: Bundle, name: String, clazz: Class<T>): T {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            bundle.getSerializable(name, clazz)!!
        else
            bundle.getSerializable(name) as T
    }

    //because the getParcelable is deprecated on android >=33. So we need to have 2 options
    fun <T : Parcelable?> getParcelable(bundle: Bundle, name: String, clazz: Class<T>): T {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            bundle.getParcelable(name, clazz)!!
        else
            bundle.getParcelable(name)!!
    }

    //get screen width in pixel
    fun getScreenWidth(): Int {
        val metrics: DisplayMetrics = MyApplication.instance().applicationContext.resources.displayMetrics
        return metrics.widthPixels
    }

    //convert dp size to pixel size
    fun convertDPtoPixel(dp: Int): Int {
        // Converts 14 dip into its equivalent px
        // Converts 14 dip into its equivalent px
        val r: Resources = MyApplication.instance().resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            r.displayMetrics
        ).toInt()
    }

    fun getImageUrl(url: String?): String? {
        if (url?.isNotEmpty() == true) {
            if (!url.contains("http", false)) {
                return BuildConfig.BaseImageUrl + url
            }
        }
        return url
    }

    // //find the newest Youtube trailer
    fun getTrailerVideo(videos: List<Video>?): Video? {
        if (videos?.isNotEmpty() == true) {
            for (i in videos.size - 1 downTo 0) {     //to get the newest one
                if (videos[i].isTrailer()) {
                    return videos[i]
                }
            }
        }
        return null
    }

    fun hideSoftKeyboard(activity: Activity?) {
        val inputMethodManager: InputMethodManager = activity?.getSystemService(
            Activity.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        if (inputMethodManager.isAcceptingText) {
            inputMethodManager.hideSoftInputFromWindow(
                activity.currentFocus?.windowToken,
                0
            )
        }
    }

    fun openWebPage(url: String?, context: Context?) {
        context?.let { context ->
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            context.startActivity(i)
        }
    }
}