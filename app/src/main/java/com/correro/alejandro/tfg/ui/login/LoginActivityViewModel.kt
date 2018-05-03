package com.correro.alejandro.tfg.ui.login

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.correro.alejandro.tfg.data.api.ApiClient
import com.correro.alejandro.tfg.data.api.ApiService
import com.correro.alejandro.tfg.data.api.models.LoginResponse
import com.correro.alejandro.tfg.data.api.models.chronicresponse.Chronic
import com.correro.alejandro.tfg.data.api.models.chronicresponse.ChronicResponse
import com.correro.alejandro.tfg.data.api.models.historialresponse.Historical
import com.correro.alejandro.tfg.data.api.models.historialresponse.HistoricalResponse
import com.correro.alejandro.tfg.data.api.models.userresponse.User
import com.correro.alejandro.tfg.data.api.models.userresponse.UserResponse
import com.correro.alejandro.tfg.utils.Constants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException


class LoginActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val apiService: ApiService = ApiClient.getInstance(application.applicationContext).getService()
    lateinit var token: String
    lateinit var userResponse: UserResponse
    lateinit var errorCode: MutableLiveData<Int>
    var cox: Application = application
    lateinit var historicalResponse: ArrayList<Historical>
     lateinit var chronicsResponse: ArrayList<Chronic>
    lateinit var allValues: MutableLiveData<Boolean>

    fun login(username: String, password: String) {
        errorCode = MutableLiveData()
        allValues = MutableLiveData()
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
        Constants.token="Bearer "+loginResponse.accessToken
        apiService.getUser("Bearer $token").observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setUser, this::setError)

    }


    fun setChronics(chronicResponse: ChronicResponse) {
        this.chronicsResponse = chronicResponse.chronics
        allValues.value = true
    }


    private fun setHistoricals(historicalResponse: HistoricalResponse) {
        this.historicalResponse = historicalResponse.historicals
        apiService.getChronics("Bearer $token").observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setChronics, this::setError)
    }

    private fun setUser(userResponse: UserResponse) {
        this.userResponse = userResponse
        apiService.getHistorical("Bearer $token").observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setHistoricals, this::setError)
    }

    init {
        val apiService: ApiService = ApiClient.getInstance(application.applicationContext).getService()
    }


}

