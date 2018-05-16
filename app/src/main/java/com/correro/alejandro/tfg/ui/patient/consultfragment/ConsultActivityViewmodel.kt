package com.correro.alejandro.tfg.ui.patient.consultfragment

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.correro.alejandro.tfg.data.api.ApiClient
import com.correro.alejandro.tfg.data.api.ApiService
import com.correro.alejandro.tfg.data.api.models.createconsultresponse.CreateConsultResponse
import com.correro.alejandro.tfg.data.api.models.specialtiesresponse.SpecialtiesResponse
import com.correro.alejandro.tfg.data.api.models.specialtiesresponse.Specialty
import com.correro.alejandro.tfg.data.api.models.userresponse.User
import com.correro.alejandro.tfg.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import retrofit2.HttpException
import retrofit2.http.Part
import java.io.IOException
import java.net.SocketTimeoutException
import kotlin.collections.ArrayList


class ConsultActivityViewmodel(application: Application) : AndroidViewModel(application) {
    private val apiService: ApiService = ApiClient.getInstance(application.applicationContext).getService()
    lateinit var errorMessage: MutableLiveData<String>
    lateinit var speacilties: MutableLiveData<ArrayList<Specialty>>
    lateinit var createConsult: MutableLiveData<Boolean>
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
        val builder = MultipartBody.Builder()
        builder.addFormDataPart("description", descripcion)
        builder.addFormDataPart("id_medic", idMedico)
        builder.setType(MultipartBody.FORM)
        if (photos.size!=0) {
            for (i in photos.size - 1 downTo 0) {
                builder.addFormDataPart("file[$i]", "adjunto consulta", photos[i].photo.body())
            }
        }
        val finalRequestBody = builder.build()
        apiService.createConsultMEdic(Constants.token,finalRequestBody).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setConsulResponse, this::setError)









    }


    fun setConsulResponse(createConsultResponse: CreateConsultResponse) {
        if (createConsultResponse.status == Constants.HTTP_CREATED) {
            createConsult.value = true
        } else {
            errorMessage.value = createConsultResponse.message
        }

    }
}


