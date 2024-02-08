package com.datnguyen.movie_dev.extras.enums

import java.util.*


enum class HomeType(val value: Int) {
    POPULAR(0),
    NOW_PLAYING(1),
    UPCOMING(2),
    TOP_RATED(3),

    ;


    companion object {
        private val map = HashMap<Int, HomeType>()

        init {
            for (homeType in values()) {
                map[homeType.value] = homeType
            }
        }

        //get Home Type enum from value
        fun fromValue(value: Int): HomeType? {
            return if (map[value] != null) {
                map[value]
            } else map[0]
        }
    }
}
