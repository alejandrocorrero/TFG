package com.correro.alejandro.tfg.ui.patient.citatefragment

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.widget.Toast
import com.correro.alejandro.tfg.data.api.ApiClient
import com.correro.alejandro.tfg.data.api.ApiService
import com.correro.alejandro.tfg.data.api.models.citattionsmedicresponse.CitationMedicUsed
import com.correro.alejandro.tfg.data.api.models.citattionsmedicresponse.CitationMedicResponse
import com.correro.alejandro.tfg.data.api.models.createcitationresponse.CitationCreatedResponse
import com.correro.alejandro.tfg.data.api.models.medichoraryresponse.Horary
import com.correro.alejandro.tfg.data.api.models.medichoraryresponse.MedicHoraryResponse
import com.correro.alejandro.tfg.utils.Constants
import com.wdullaer.materialdatetimepicker.time.Timepoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class CitationActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val apiService: ApiService = ApiClient.getInstance(application.applicationContext).getService()
    lateinit var errorMessage: MutableLiveData<String>
    lateinit var citatitons: ArrayList<CitationMedicUsed>
    lateinit var horary: MutableLiveData<ArrayList<Horary>>
    lateinit var citationCreated: MutableLiveData<Boolean>
    var minute = SimpleDateFormat("HH:mm:ss")
    var nameDay = SimpleDateFormat("EEEE", Locale("es", "ES"))
    lateinit var horaryMedic: ArrayList<HoraryMedic>

    fun getCitationsMedic() {
        horary = MutableLiveData()
        apiService.getCitationsMedicUsed(Constants.token).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setCitations, this::setError)

    }

    private fun setCitations(citationMedicResponse: CitationMedicResponse) {
        if (citationMedicResponse.status == Constants.HTTP_OK) {
            citatitons = citationMedicResponse.citationMedicUsed
            apiService.getHoraryMedic(Constants.token).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setHorary, this::setError)
        }
    }

    private fun setHorary(medicHoraryResponse: MedicHoraryResponse) {
        if (medicHoraryResponse.status == Constants.HTTP_OK) {
            horaryMedic = ArrayList()
            medicHoraryResponse.data[0].dia
            val today = Calendar.getInstance()
            for (i in 0..7) {
                for (y in medicHoraryResponse.data) {
                    var day = nameDay.format(today.time)
                    if (day.equals(y.dia, true)) {
                        horaryMedic.add(HoraryMedic(today.clone() as Calendar))
                        val horaInicio = Calendar.getInstance()
                        val horaFin = Calendar.getInstance()
                        horaInicio.time = minute.parse(y.horaInicio)
                        horaFin.time = minute.parse(y.horaFin)
                        while (horaInicio.time.compareTo(horaFin.time) == -1) {
                            horaryMedic.last().hours.add(minute.format(horaInicio.time))
                            horaInicio.add(Calendar.MINUTE, 12)
                        }
                    }

                }
                today.add(Calendar.DATE, 1)
            }
            horary.value = medicHoraryResponse.data
        }
    }

    private fun setError(e: Throwable?) {
        when (e) {
            is HttpException -> errorMessage.value = "Try again"
            is SocketTimeoutException -> errorMessage.value = "Try again"
            is IOException -> errorMessage.value = "IO error"
        }
    }

    fun createCitation(date: String, time: String) {
        if (!::citationCreated.isInitialized) {
            citationCreated = MutableLiveData()
        }
        if (!::errorMessage.isInitialized) {
            errorMessage = MutableLiveData()
        }
        apiService.createCitation(Constants.token, date, time).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setCreate, this::setError)

    }

    private fun setCreate(response: CitationCreatedResponse) {
        if (response.status == Constants.HTTP_CREATED)
            citationCreated.value = true
        if (response.status == Constants.HTTP_NOT_FOUND)
            errorMessage.value = "La cita elegida ya existe"
    }

}