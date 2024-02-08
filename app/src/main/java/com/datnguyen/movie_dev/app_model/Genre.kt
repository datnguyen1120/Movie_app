package com.datnguyen.movie_dev.app_model

import kotlinx.parcelize.Parcelize

@Parcelize
data class Genre(val id: Int, val name: String): BaseModel()
