package com.datnguyen.movie_dev.services.api_service

import com.datnguyen.movie_dev.services.RetrofitService
import com.datnguyen.movie_dev.services.model.*
import retrofit2.http.*

interface AuthenticationService {
    companion object {
        var authenticationService: AuthenticationService? = null
        fun getInstance(): AuthenticationService {
            if (authenticationService == null) {
                val retrofit = RetrofitService.getInstance()
                authenticationService = retrofit?.create(AuthenticationService::class.java)
            }
            return authenticationService!!
        }
    }

    @GET("authentication/token/new")
    suspend fun requestToken(): TokenResponse

    @POST("authentication/token/validate_with_login")
    suspend fun requestLogin(@Body loginRequest: LoginRequest): TokenResponse

    @POST("authentication/session/new")
    suspend fun createSession(@Body loginRequest: LoginRequest): SessionResponse

    //the retrofit does not allow DELETE with request body
    //so we do the trick delete with body
    @HTTP(method = "DELETE", path = "authentication/session", hasBody = true)
    suspend fun deleteSession(@Body request: DeleteSessionRequest): SessionResponse

    @GET("account")
    suspend fun getDetails(): AccountResponse
}