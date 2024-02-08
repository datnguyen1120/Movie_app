package com.datnguyen.movie_dev.services

import com.datnguyen.movie_dev.BuildConfig
import com.datnguyen.movie_dev.MyApplication
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


interface RetrofitService {
    companion object {
        var retrofit: Retrofit? = null
        fun getInstance(): Retrofit? {
            if (retrofit == null) {
                val builder = OkHttpClient.Builder()

                //add base query and header
                builder.addInterceptor(Interceptor { chain ->
                    val request: Request.Builder = chain.request().newBuilder()
                    val originalHttpUrl = chain.request().url

                    //add api key
                    val builder = originalHttpUrl.newBuilder()
                        .addQueryParameter("api_key", BuildConfig.APIKey)

                    //add session id
                    MyApplication.instance().session?.sessionId?.let {
                        builder.addQueryParameter("session_id", it)
                    }

                    request.url(builder.build())
                    chain.proceed(request.build())
                })

                //logging
                val interceptor = HttpLoggingInterceptor()
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                builder.addInterceptor(interceptor)

                // Creating a client out of the builder
                val client: OkHttpClient = builder.build()

                retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.APIServer)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
    }
}