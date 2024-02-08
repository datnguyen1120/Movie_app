package com.datnguyen.movie_dev.services.api_repository

import com.datnguyen.movie_dev.services.api_service.AuthenticationService
import com.datnguyen.movie_dev.services.model.DeleteSessionRequest
import com.datnguyen.movie_dev.services.model.LoginRequest

class AuthenticationRepository constructor(private val authenticationService: AuthenticationService) {
    suspend fun requestToken() = authenticationService.requestToken()
    suspend fun login(request: LoginRequest) = authenticationService.requestLogin(request)
    suspend fun createSession(request: LoginRequest) = authenticationService.createSession(request)
    suspend fun deleteSession(request: DeleteSessionRequest) = authenticationService.deleteSession(request)
    suspend fun getAccountDetails() = authenticationService.getDetails()
}