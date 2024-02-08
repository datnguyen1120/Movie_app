package com.datnguyen.movie_dev.services.api_repository

import com.datnguyen.movie_dev.services.api_service.MovieService

class MovieRepository constructor(private val movieService: MovieService) {
    suspend fun getPopular(page: Int) = movieService.getPopular(page)
    suspend fun getNowPlaying(page: Int) = movieService.getNowPlaying(page)
    suspend fun getUpcoming(page: Int) = movieService.getUpcoming(page)
    suspend fun getTopRated(page: Int) = movieService.getTopRated(page)

    suspend fun searchMovies(query: String) = movieService.searchMovies(query)

    suspend fun getMovieDetails(movieId: Int) = movieService.getMovieDetails(movieId)
    suspend fun getMovieCredits(movieId: Int) = movieService.getMovieCredits(movieId)
    suspend fun getMovieVideos(movieId: Int) = movieService.getMovieVideos(movieId)
}