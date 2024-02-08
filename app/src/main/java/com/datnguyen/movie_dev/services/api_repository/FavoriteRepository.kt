package com.datnguyen.movie_dev.services.api_repository

import com.datnguyen.movie_dev.services.api_service.FavoriteService
import com.datnguyen.movie_dev.services.model.FavoriteMovieRequest

class FavoriteRepository constructor(private val favoriteService: FavoriteService) {
    suspend fun markFavorite(accountId: Int, request: FavoriteMovieRequest) = favoriteService.markFavorite(accountId, request)
    suspend fun getFavoriteMovies(accountId: Int, page: Int) = favoriteService.getFavoriteMovies(accountId, page)
    suspend fun getMovieState(movieId: Int) = favoriteService.getMovieState(movieId)
}