package com.datnguyen.movie_dev

import androidx.multidex.MultiDexApplication
import com.datnguyen.movie_dev.app_model.Session
import com.datnguyen.movie_dev.extras.Constants
import com.orhanobut.hawk.Hawk


class MyApplication : MultiDexApplication() {
    companion object {
        private lateinit var myApplication: MyApplication

        fun instance(): MyApplication {
            return myApplication
        }
    }

    private var _session: Session? = null
    val session get() = _session

    init {
        myApplication = this
    }

    override fun onCreate() {
        super.onCreate()
        //init hawk
        //https://github.com/orhanobut/hawk
        Hawk.init(this).build()

        loadSession()
    }

    fun loadSession() {
        //load saved login session
        _session = Hawk.get<Session>(Constants.HAWK_SESSION, null)
    }

    fun saveSession(session: Session) {
        Hawk.put(Constants.HAWK_SESSION, session)
        loadSession()
    }

    fun cleanSession() {
        Hawk.delete(Constants.HAWK_SESSION)
        loadSession()
    }
}
