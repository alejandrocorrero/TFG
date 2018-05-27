package com.correro.alejandro.tfg.ui.medic.econsultfragment

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.correro.alejandro.tfg.data.api.ApiClient
import com.correro.alejandro.tfg.data.api.ApiService
import com.correro.alejandro.tfg.data.api.models.createconsultresponse.CreateConsultResponse
import com.correro.alejandro.tfg.data.api.models.medicusersresponse.MedicUser
import com.correro.alejandro.tfg.data.api.models.medicusersresponse.MedicUserResponse
import com.correro.alejandro.tfg.data.api.models.specialtiesresponse.SpecialtiesResponse
import com.correro.alejandro.tfg.data.api.models.specialtiesresponse.Specialty
import com.correro.alejandro.tfg.data.api.models.userresponse.User
import com.correro.alejandro.tfg.ui.patient.consultfragment.ImageItem
import com.correro.alejandro.tfg.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import kotlin.collections.ArrayList


class EConsultAddViewmodel(application: Application) : AndroidViewModel(application) {
    private val apiService: ApiService = ApiClient.getInstance(application.applicationContext).getService()
    lateinit var errorMessage: MutableLiveData<String>
    lateinit var speacilties: MutableLiveData<ArrayList<Specialty>>
    lateinit var createEConsultResponse: MutableLiveData<Boolean>
    var photos: ArrayList<ImageItem> = ArrayList()
    lateinit var users: MutableLiveData<ArrayList<MedicUser>>

    var pref= application.getSharedPreferences(Constants.PREFERENCES,0)!!

    fun getSpecialties(): MutableLiveData<ArrayList<Specialty>> {
        if (!::speacilties.isInitialized) {
            errorMessage = MutableLiveData()
            speacilties = MutableLiveData()
            apiService.getSpecialties(pref.getString(Constants.TOKEN_CONSTANT,"")).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setSpecialties, this::setError)
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

    fun createEconsult(descripcion: String,idSpecialty: String,idPatient: String) {
        createEConsultResponse = MutableLiveData()
        errorMessage = MutableLiveData()
        val builder = MultipartBody.Builder()
        builder.addFormDataPart("description", descripcion)
        builder.addFormDataPart("id_specialty", idSpecialty)
        builder.addFormDataPart("id_patient", idPatient)
        builder.setType(MultipartBody.FORM)
        if (photos.size!=0) {
            for (i in photos.size - 1 downTo 0) {
                builder.addFormDataPart("file[$i]", "adjunto consulta", photos[i].photo.body())
            }
        }
        val finalRequestBody = builder.build()
        apiService.createEconsult(pref.getString(Constants.TOKEN_CONSTANT,""),finalRequestBody).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setConsulResponse, this::setError)

    }


    fun setConsulResponse(createConsultResponse: CreateConsultResponse) {
        if (createConsultResponse.status == Constants.HTTP_CREATED) {
            createEConsultResponse.value = true
        } else {
            errorMessage.value = createConsultResponse.message
        }

    }
    fun getUsers(filter: String?): MutableLiveData<ArrayList<MedicUser>> {
        users = MutableLiveData()
        errorMessage = MutableLiveData()
        apiService.getUsers(pref.getString(Constants.TOKEN_CONSTANT, ""), filter).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setusers, this::setError)
        return users
    }

    private fun setusers(userResponse: MedicUserResponse) {
        if (userResponse.status == Constants.HTTP_OK) {
            users.value = userResponse.users

        }
    }
}


