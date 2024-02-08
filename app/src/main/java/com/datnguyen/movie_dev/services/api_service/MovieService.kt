package com.datnguyen.movie_dev.services.api_service

import com.datnguyen.movie_dev.app_model.Movie
import com.datnguyen.movie_dev.app_model.Video
import com.datnguyen.movie_dev.services.RetrofitService
import com.datnguyen.movie_dev.services.model.CreditsResponse
import com.datnguyen.movie_dev.services.model.ResponseModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
    companion object {
        var movieService: MovieService? = null
        fun getInstance(): MovieService {
            if (movieService == null) {
                val retrofit = RetrofitService.getInstance()
                movieService = retrofit?.create(MovieService::class.java)
            }
            return movieService!!
        }
    }

    @GET("search/movie")
    suspend fun searchMovies(@Query("query") query: String): ResponseModel<List<Movie>>

    @GET("movie/popular")
    suspend fun getPopular(@Query("page") page: Int): ResponseModel<List<Movie>>

    @GET("movie/now_playing")
    suspend fun getNowPlaying(@Query("page") page: Int): ResponseModel<List<Movie>>

    @GET("movie/top_rated")
    suspend fun getTopRated(@Query("page") page: Int): ResponseModel<List<Movie>>

    @GET("movie/upcoming")
    suspend fun getUpcoming(@Query("page") page: Int): ResponseModel<List<Movie>>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Int): Movie

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(@Path("movie_id") movieId: Int): CreditsResponse

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(@Path("movie_id") movieId: Int): ResponseModel<List<Video>>

}