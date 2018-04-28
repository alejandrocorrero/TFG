package com.correro.alejandro.tfg.ui.login

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.correro.alejandro.tfg.data.api.ApiClient
import com.correro.alejandro.tfg.data.api.ApiService
import com.correro.alejandro.tfg.data.api.models.LoginResponse
import com.correro.alejandro.tfg.data.api.models.historialresponse.HistoricalResponse
import com.correro.alejandro.tfg.data.api.models.userresponse.UserResponse
import com.correro.alejandro.tfg.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException


class LoginActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val apiService: ApiService = ApiClient.getInstance(application.applicationContext).getService()
    lateinit var token: String
    lateinit var userResponse: MutableLiveData<UserResponse>
    lateinit var errorCode: MutableLiveData<Int>
    var cox: Application = application
    lateinit var historicalResponse: MutableLiveData<HistoricalResponse>

    fun login(username: String, password: String) {
        userResponse = MutableLiveData()
        errorCode = MutableLiveData()
        apiService.login(username, password, Constants.TYPE, Constants.CLIENT_ID, Constants.CLIENT_SECRET).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setLogin, this::setError)
    }

    private fun setError(e: Throwable?) {
        when (e) {
            is HttpException -> errorCode.value = e.response().code()
            is SocketTimeoutException -> errorCode.value = 408
            is IOException -> errorCode.value = 404
        }
    }


    private fun setLogin(loginResponse: LoginResponse) {
        token = loginResponse.accessToken
        getUser()
    }

    fun getUser() {
        apiService.getUser("Bearer $token").observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setUser, this::setError)

    }
    fun getHistoricals(){
        historicalResponse= MutableLiveData()
        apiService.getHistorical("Bearer $token").observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setHistoricals, this::setError)
    }



    private fun setHistoricals(historicalResponse: HistoricalResponse) {
        this.historicalResponse.value=historicalResponse
    }

    private fun setUser(userResponse: UserResponse) {
        this.userResponse.value = userResponse
    }


}

