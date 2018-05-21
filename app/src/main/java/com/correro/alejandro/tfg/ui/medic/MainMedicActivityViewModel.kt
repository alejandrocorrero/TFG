package com.correro.alejandro.tfg.ui.medic

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.correro.alejandro.tfg.data.api.ApiClient
import com.correro.alejandro.tfg.data.api.ApiService
import com.correro.alejandro.tfg.data.api.models.consultslistresponse.ConsultsList
import com.correro.alejandro.tfg.data.api.models.consultslistresponse.ConsultsListResponse
import com.correro.alejandro.tfg.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class MainMedicActivityViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var consults: MutableLiveData<ArrayList<ConsultsList>>
    private val apiService: ApiService = ApiClient.getInstance(application.applicationContext).getService()
    lateinit var errorMessage: MutableLiveData<String>

    fun getConsultsPatiens() {
        errorMessage = MutableLiveData()
        consults = MutableLiveData()
        apiService.getConsultsPatiens(Constants.token).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setConsults, this::setError)

    }
    fun getConsultSspecialty() {
        errorMessage = MutableLiveData()
        consults = MutableLiveData()
        apiService.getConsultsSpecialty(Constants.token).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setConsults, this::setError)

    }
    private fun setConsults(consultsListResponse: ConsultsListResponse) {
        if (consultsListResponse.status == Constants.HTTP_OK) {
            consults.value = consultsListResponse.consults
        }else if (consultsListResponse.status == Constants.HTTP_NOT_FOUND) {
            errorMessage.value = consultsListResponse.message
        }
    }
    private fun setError(e: Throwable?) {
        when (e) {
            is HttpException -> errorMessage.value = "Try again"
            is SocketTimeoutException -> errorMessage.value = "Try again"
            is IOException -> errorMessage.value = "IO error"
        }
    }
}
