package com.correro.alejandro.tfg.ui.medic

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.data.api.ApiClient
import com.correro.alejandro.tfg.data.api.ApiService
import com.correro.alejandro.tfg.data.api.models.citationresponse.Citation
import com.correro.alejandro.tfg.data.api.models.citationresponse.CitationResponse
import com.correro.alejandro.tfg.data.api.models.consultslistresponse.ConsultsList
import com.correro.alejandro.tfg.data.api.models.consultslistresponse.ConsultsListResponse
import com.correro.alejandro.tfg.data.api.models.econsultresponse.Econsult
import com.correro.alejandro.tfg.data.api.models.econsultresponse.EconsultResponse
import com.correro.alejandro.tfg.data.api.models.medicusersresponse.MedicUser
import com.correro.alejandro.tfg.data.api.models.medicusersresponse.MedicUserResponse
import com.correro.alejandro.tfg.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class MainMedicActivityViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var consults: MutableLiveData<ArrayList<ConsultsList>>
    lateinit var econsults: MutableLiveData<ArrayList<Econsult>>
    private val apiService: ApiService = ApiClient.getInstance(application.applicationContext).getService()
    lateinit var errorMessage: MutableLiveData<String>
    lateinit var citatitons: MutableLiveData<ArrayList<Citation>>
    lateinit var users: MutableLiveData<ArrayList<MedicUser>>
    var composite: CompositeDisposable = CompositeDisposable()
    var maxConsults = 0
    var maxEConsults = 0
    var pref = application.getSharedPreferences(Constants.PREFERENCES, 0)!!
    var selectedTab: Int = R.id.mnuDiary

    fun getEConsults(position: Int) {
        errorMessage = MutableLiveData()
        econsults = MutableLiveData()
        apiService.getEConsults(pref.getString(Constants.TOKEN_CONSTANT, ""), position).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setEConsults, this::setError)

    }

    fun getEConsultSspecialty(position: Int) {
        errorMessage = MutableLiveData()
        econsults = MutableLiveData()
        apiService.getEConsultsSpecialty(pref.getString(Constants.TOKEN_CONSTANT, ""), position).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setEConsults, this::setError)

    }

    private fun setEConsults(econsultResponse: EconsultResponse) {
        if (econsultResponse.status == Constants.HTTP_OK) {
            econsults.value = econsultResponse.dataEconsults.consults
            maxEConsults=econsultResponse.dataEconsults.count
        } else if (econsultResponse.status == Constants.HTTP_NOT_FOUND) {
            errorMessage.value = econsultResponse.message
        }
    }

    fun getConsultsPatiens(position: Int) {
        errorMessage = MutableLiveData()
        consults = MutableLiveData()
        apiService.getConsultsPatiens(pref.getString(Constants.TOKEN_CONSTANT, ""), position).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setConsults, this::setError)

    }

    fun getConsultSspecialty(position: Int) {
        errorMessage = MutableLiveData()
        consults = MutableLiveData()
        apiService.getConsultsSpecialty(pref.getString(Constants.TOKEN_CONSTANT, ""), position).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setConsults, this::setError)

    }

    fun getCitations() {
        citatitons = MutableLiveData()
        errorMessage = MutableLiveData()
        apiService.getCitationsMedic(pref.getString(Constants.TOKEN_CONSTANT, "")).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setCitations, this::setError)

    }

    private fun setCitations(citationResponse: CitationResponse) {
        if (citationResponse.status == Constants.HTTP_OK) {
            citatitons.value = citationResponse.citations
        } else if (citationResponse.status == Constants.HTTP_NOT_FOUND) {
            errorMessage.value = citationResponse.message
        }
    }


    private fun setConsults(consultsListResponse: ConsultsListResponse) {
        if (consultsListResponse.status == Constants.HTTP_OK) {
            consults.value = consultsListResponse.data.consults
            maxConsults = consultsListResponse.data.count
        } else if (consultsListResponse.status == Constants.HTTP_NOT_FOUND) {
            errorMessage.value = consultsListResponse.message
        }
    }

    private fun setError(e: Throwable?) {
        when (e) {
            is HttpException -> errorMessage.value = "Prueba de nuevo"
            is SocketTimeoutException -> errorMessage.value = "Prueba de nuevo"
            is IOException -> errorMessage.value = "IO error"
        }
    }


    fun getUsers(filter: String?): MutableLiveData<ArrayList<MedicUser>> {
        users = MutableLiveData()
        errorMessage = MutableLiveData()

        composite.add(apiService.getUsers(pref.getString(Constants.TOKEN_CONSTANT, ""), filter).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setusers, this::setError))
        return users
    }

    private fun setusers(userResponse: MedicUserResponse) {
        if (userResponse.status == Constants.HTTP_OK) {
            users.value = userResponse.users

        }
    }
}
