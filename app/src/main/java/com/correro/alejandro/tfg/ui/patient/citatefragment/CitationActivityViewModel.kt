package com.correro.alejandro.tfg.ui.patient.citatefragment

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.correro.alejandro.tfg.data.api.ApiClient
import com.correro.alejandro.tfg.data.api.ApiService
import com.correro.alejandro.tfg.data.api.models.citattionsmedicresponse.CitationMedicUsed
import com.correro.alejandro.tfg.data.api.models.citattionsmedicresponse.CitationMedicResponse
import com.correro.alejandro.tfg.data.api.models.medichoraryresponse.MedicHoraryResponse
import com.correro.alejandro.tfg.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class CitationActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val apiService: ApiService = ApiClient.getInstance(application.applicationContext).getService()
    lateinit var errorMessage: MutableLiveData<String>
    lateinit var citatitons: MutableLiveData<ArrayList<CitationMedicUsed>>

    fun getCitationsMedic() {
        citatitons = MutableLiveData()
        apiService.getCitationsMedicUsed(Constants.token).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setCitations, this::setError)

    }

    private fun setCitations(citationMedicResponse: CitationMedicResponse) {
        if (citationMedicResponse.status == Constants.HTTP_OK) {
            citatitons.value = citationMedicResponse.citationMedicUsed
            apiService.getHoraryMedic(Constants.token).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setHorary, this::setError)
        }
    }

    //TODO TERMINAR OBTENER HORARIO DIVIDIRLO ENTRE 12MINUTOS Y ELIMINAR LAS CITAS USADAS CREAR LIVEDATE DE CITAS DISPONIBLES
    private fun setHorary(medicHoraryResponse: MedicHoraryResponse) {
        if (medicHoraryResponse.status == Constants.HTTP_OK) {

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