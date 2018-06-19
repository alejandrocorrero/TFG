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
import com.correro.alejandro.tfg.data.api.models.userresponse.UserResponse
import com.correro.alejandro.tfg.utils.Constants
import com.correro.alejandro.tfg.utils.Constants.Companion.PREFERENCES
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
    lateinit var historicalResponse: ArrayList<Historical>
    lateinit var chronicsResponse: ArrayList<Chronic>
    lateinit var allValues: MutableLiveData<Boolean>
    private var usertype: Int = 0
    fun loginCall(username: String, password: String) {
        errorCode = MutableLiveData()
        userResponse = MutableLiveData()
        apiService.login(username, password, Constants.TYPE, Constants.CLIENT_ID, Constants.CLIENT_SECRET).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::responseLogin, this::setError)
    }

    fun loginMedicCall() {
        errorCode = MutableLiveData()
        userResponse = MutableLiveData()
        apiService.getUser(token).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::responseUser, this::setError)

    }

    private fun setError(e: Throwable?) {
        when (e) {
            is HttpException -> errorCode.value = e.response().code()
            is SocketTimeoutException -> errorCode.value = 408
            is IOException -> errorCode.value = 404
        }
    }


    private fun responseLogin(loginResponse: LoginResponse) {
        token = "Bearer " + loginResponse.accessToken
        cox.getSharedPreferences(PREFERENCES, 0).edit().putString(Constants.TOKEN_CONSTANT, token).apply()

        apiService.getUser(token).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::responseUser, this::setError)

    }


    fun responseChronics(chronicResponse: ChronicResponse) {
        this.chronicsResponse = chronicResponse.chronics
        allValues.value = true
    }


    private fun responseHistoricals(historicalResponse: HistoricalResponse) {
        this.historicalResponse = historicalResponse.dataHistorial.historicals

        apiService.getChronics(token).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::responseChronics, this::setError)
    }


    private fun responseUser(userResponse: UserResponse) {
        this.userResponse.value = userResponse
        this.usertype = userResponse.type
        cox.getSharedPreferences(PREFERENCES, 0).edit().putInt(Constants.TYPE_CONSTAN, userResponse.type).apply()
    }

    public fun responseValues(): MutableLiveData<Boolean> {
        allValues = MutableLiveData()
        apiService.getHistorical(token,0).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::responseHistoricals, this::setError)
        return allValues
    }


}

