package com.correro.alejandro.tfg.login

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.correro.alejandro.tfg.api.ApiClient
import com.correro.alejandro.tfg.api.ApiService
import com.correro.alejandro.tfg.api.models.LoginResponse
import com.correro.alejandro.tfg.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException


class LoginActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val apiService: ApiService = ApiClient.getInstance(application.applicationContext).getService()
    lateinit var token: MutableLiveData<String>
    fun login(username: String, password: String) {
        token = MutableLiveData()
        apiService.login(username, password, Constants.TYPE, Constants.CLIENT_ID, Constants.CLIENT_SECRET).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setLogin, this::setError)
    }

    private fun setError(e: Throwable?) {
        if (e is HttpException) {
            val responseBody = e.response().errorBody()
        } else if (e is SocketTimeoutException) {
        } else if (e is IOException) {
        } else {
        }

    }



    fun setLogin(loginResponse: LoginResponse) {
        token.value = loginResponse.accessToken

    }

}

