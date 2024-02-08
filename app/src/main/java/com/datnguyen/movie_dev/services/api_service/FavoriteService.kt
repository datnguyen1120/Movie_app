package com.datnguyen.movie_dev.services.api_service

import com.datnguyen.movie_dev.app_model.BaseModel
import com.datnguyen.movie_dev.app_model.Movie
import com.datnguyen.movie_dev.app_model.MovieState
import com.datnguyen.movie_dev.services.RetrofitService
import com.datnguyen.movie_dev.services.model.FavoriteMovieRequest
import com.datnguyen.movie_dev.services.model.ResponseModel
import retrofit2.http.*

interface FavoriteService {
    companion object {
        var favoriteService: FavoriteService? = null
        fun getInstance(): FavoriteService {
            if (favoriteService == null) {
                val retrofit = RetrofitService.getInstance()
                favoriteService = retrofit?.create(FavoriteService::class.java)
            }
            return favoriteService!!
        }
    }

    @POST("account/{accountId}/favorite")
    suspend fun markFavorite(@Path("accountId") accountId: Int,
                             @Body request: FavoriteMovieRequest): BaseModel

    @GET("account/{accountId}/favorite/movies")
    suspend fun getFavoriteMovies(@Path("accountId") accountId: Int,
                                  @Query("page") page: Int): ResponseModel<List<Movie>>

    @GET("movie/{movieId}/account_states")
    suspend fun getMovieState(@Path("movieId") movieId: Int): MovieState
}