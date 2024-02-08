package com.datnguyen.movie_dev.ui.fragment.signin

import com.datnguyen.movie_dev.MyApplication
import com.datnguyen.movie_dev.app_model.Session
import com.datnguyen.movie_dev.extras.LiveEvent
import com.datnguyen.movie_dev.services.api_repository.AuthenticationRepository
import com.datnguyen.movie_dev.services.api_service.AuthenticationService
import com.datnguyen.movie_dev.services.model.LoginRequest
import com.datnguyen.movie_dev.ui.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignInViewModel : BaseViewModel() {
    var authenticationRepository: AuthenticationRepository
    var requestToken: String? = null

    var loginSuccess = LiveEvent<Boolean>()
    var username = LiveEvent<String>()
    var password = LiveEvent<String>()

    init {
        val authenticationService = AuthenticationService.getInstance()
        authenticationRepository = AuthenticationRepository(authenticationService)

        requestToken()
    }

    //step 1
    private fun requestToken() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = authenticationRepository.requestToken()
            withContext(Dispatchers.Main) {
                if (response.success != false) {
                    requestToken = response.requestToken
                } else {
                    onError(response.statusMessage)
                    loading.postValue(false)
                }
            }
        }
    }

    //step 2
    fun login() {
        loading.postValue(true)
        val request = LoginRequest(username.value, password.value, requestToken)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = authenticationRepository.login(request)
            withContext(Dispatchers.Main) {
                if (response.success != false) {
                    requestToken = response.requestToken

                    generateSession()
                } else {
                    onError(response.statusMessage)
                    loading.postValue(false)
                }
            }
        }
    }

    //step 3
    private fun generateSession() {
        val request = LoginRequest(null, null, requestToken)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = authenticationRepository.createSession(request)
            withContext(Dispatchers.Main) {
                if (response.success != false) {
                    val sessionId = response.sessionId

                    //save session
                    val session = Session(sessionId)
                    MyApplication.instance().saveSession(session)

                    //get account details
                    getAccountDetails()
                } else {
                    onError(response.statusMessage)
                    loading.postValue(false)
                }
            }
        }
    }

    private fun getAccountDetails() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = authenticationRepository.getAccountDetails()
            withContext(Dispatchers.Main) {
                if (response.success != false) {
                    val session = MyApplication.instance().session ?: Session()
                    session.accountId = response.id
                    session.name = response.name
                    session.username = response.username
                    session.avatarUrl = response.getAvatarUrl()
                    MyApplication.instance().saveSession(session)

                    loginSuccess.postValue(true)
                } else {
                    onError(response.statusMessage)
                }
                loading.postValue(false)
            }
        }
    }

    fun onUsernameTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        username.postValue(s.toString())
    }

    fun onPasswordTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        password.postValue(s.toString())
    }
}