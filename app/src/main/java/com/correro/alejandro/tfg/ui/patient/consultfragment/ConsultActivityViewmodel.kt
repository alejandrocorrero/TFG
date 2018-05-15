package com.correro.alejandro.tfg.ui.patient.consultfragment

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.correro.alejandro.tfg.data.api.ApiClient
import com.correro.alejandro.tfg.data.api.ApiService
import com.correro.alejandro.tfg.data.api.models.attachmentsresponse.AttachmentCreatedResponse
import com.correro.alejandro.tfg.data.api.models.createconsultresponse.CreateConsultResponse
import com.correro.alejandro.tfg.data.api.models.specialtiesresponse.SpecialtiesResponse
import com.correro.alejandro.tfg.data.api.models.specialtiesresponse.Specialty
import com.correro.alejandro.tfg.data.api.models.userresponse.User
import com.correro.alejandro.tfg.utils.Constants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.*
import kotlin.collections.ArrayList

class ConsultActivityViewmodel(application: Application) : AndroidViewModel(application) {
    private val apiService: ApiService = ApiClient.getInstance(application.applicationContext).getService()
    lateinit var errorMessage: MutableLiveData<String>
    lateinit var speacilties: MutableLiveData<ArrayList<Specialty>>
    lateinit var createConsult: MutableLiveData<ArrayList<Boolean>>
    var photos: ArrayList<ImageItem> = ArrayList()
    lateinit var user: User

    fun getSpecialties(): MutableLiveData<ArrayList<Specialty>> {
        if (!::speacilties.isInitialized) {
            errorMessage = MutableLiveData()
            speacilties = MutableLiveData()
            apiService.getSpecialties(Constants.token).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setSpecialties, this::setError)
        }
        return speacilties
    }

    private fun setSpecialties(specialtiesResponse: SpecialtiesResponse) {
        if (specialtiesResponse.status == Constants.HTTP_OK) {
            speacilties.value = specialtiesResponse.specialties
        }
    }

    private fun setError(e: Throwable?) {
        when (e) {
            is HttpException -> errorMessage.value = "Try again"
            is SocketTimeoutException -> errorMessage.value = "Try again"
            is IOException -> errorMessage.value = "IO error"
        }
    }

    fun createConsultMedic(descripcion: String, idMedico: String) {
        createConsult = MutableLiveData()
        errorMessage = MutableLiveData()
       var obse= apiService.createConsultMEdic(Constants.token, descripcion, idMedico.toInt())
        var ph:ArrayList<Observable<AttachmentCreatedResponse>> = ArrayList()
        for(it:ImageItem in photos){
            ph.add(apiService.postTest(Constants.token,it.photo,"test"))
        }
    //TODO LLAMADAS



    }



    fun setConsulResponse(createConsultResponse: CreateConsultResponse) {
        if (createConsultResponse.status == Constants.HTTP_CREATED) {
            createConsult.value =
        } else {
            errorMessage.value = createConsultResponse.message
        }

    }
}


