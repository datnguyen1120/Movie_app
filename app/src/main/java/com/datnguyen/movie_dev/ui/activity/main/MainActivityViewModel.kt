package com.datnguyen.movie_dev.ui.activity.main

import com.datnguyen.movie_dev.MyApplication
import com.datnguyen.movie_dev.extras.LiveEvent
import com.datnguyen.movie_dev.services.api_repository.AuthenticationRepository
import com.datnguyen.movie_dev.services.api_service.AuthenticationService
import com.datnguyen.movie_dev.services.model.DeleteSessionRequest
import com.datnguyen.movie_dev.ui.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel() : BaseViewModel() {
    var authenticationRepository: AuthenticationRepository
    val logoutEvent = LiveEvent<Boolean?>()

    init {
        val authenticationService = AuthenticationService.getInstance()
        authenticationRepository = AuthenticationRepository(authenticationService)
    }

    fun logout() {
        val sessionId = MyApplication.instance().session?.sessionId
        sessionId?.let {
            loading.postValue(true)
            job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                val response = authenticationRepository.deleteSession(DeleteSessionRequest(it))
                withContext(Dispatchers.Main) {
                    if (response.success != false) {
                        MyApplication.instance().cleanSession()

                        logoutEvent.postValue(true)
                    } else {
                        onError(response.statusMessage)
                    }
                    loading.postValue(false)
                }
            }
        }
    }

}