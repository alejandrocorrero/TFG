package com.correro.alejandro.tfg.ui.medic.econsultfragment

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.correro.alejandro.tfg.data.api.ApiClient
import com.correro.alejandro.tfg.data.api.ApiService
import com.correro.alejandro.tfg.data.api.models.consultpatientresponse.ConsultPatientResponse
import com.correro.alejandro.tfg.data.api.models.consultpatientresponse.Respuesta
import com.correro.alejandro.tfg.data.api.models.createdResponse.ResponseResponse
import com.correro.alejandro.tfg.data.api.models.econsultdetailresponse.EconsultDetailResponse
import com.correro.alejandro.tfg.data.api.models.econsultdetailresponse.EconsultInfo
import com.correro.alejandro.tfg.ui.patient.patientfragment.attachmentsfragment.FileWithType
import com.correro.alejandro.tfg.utils.Constants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.net.SocketTimeoutException

class EConsultDetailActivityViewModel(application: Application) : AndroidViewModel(application) {
    var idEConsult: Int = -1
    private val apiService: ApiService = ApiClient.getInstance(application.applicationContext).getService()
    lateinit var errorMessage: MutableLiveData<String>
    lateinit var econsult: MutableLiveData<EconsultInfo>
    var archivo: ArrayList<MutableLiveData<FileWithType>> = ArrayList()
    lateinit var responseCreated: MutableLiveData<Respuesta>
    var pref = application.getSharedPreferences(Constants.PREFERENCES, 0)!!

    fun getEconsult() {
        errorMessage = MutableLiveData()
        econsult = MutableLiveData()
        apiService.getEconsultDetail(pref.getString(Constants.TOKEN_CONSTANT, ""), idEConsult).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setEConsult, this::setError)

    }

    private fun setError(e: Throwable?) {
        when (e) {
            is HttpException -> errorMessage.value = "Prueba de nuevo"
            is SocketTimeoutException -> errorMessage.value = "Prueba de nuevo"
            is IOException -> errorMessage.value = "IO error"
        }
    }

    private fun setEConsult(econsultDetailResponse: EconsultDetailResponse) {
        if (econsultDetailResponse.status == Constants.HTTP_OK) {
            econsult.value = econsultDetailResponse.econsultInfo
        }
    }

    fun downloadFile(url: String, v: Context): MutableLiveData<FileWithType> {
        archivo.add(MutableLiveData())
        var position = archivo.size - 1
        apiService.downloadFile(pref.getString(Constants.TOKEN_CONSTANT, ""), url).observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).flatMap({ t -> test(t, v) }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({ f -> setfile(f, position) }, this::setError)
        return archivo[position]
    }

    private fun test(response: Response<ResponseBody>, v: Context): Observable<FileWithType>? {
        return Observable.create({
            val header = response.headers().get("Content-Disposition")
            val type = response.headers().get("Content-Type")
            val fileName = header!!.replace("attachment; filename=", "").replace("\"", "")
            v.openFileOutput(fileName, Context.MODE_PRIVATE).use { it?.write(response.body()!!.source().readByteArray()) }
            val directory = v.filesDir
            val file = File(directory, fileName)
            it.onNext(FileWithType(file, type!!))
            it.onComplete()
        })
    }

    private fun setfile(file: FileWithType, position: Int) {
        archivo[position].value = file
    }

    fun newResponse(text: String): MutableLiveData<Respuesta> {
        errorMessage = MutableLiveData()
        responseCreated = MutableLiveData()

        if (idEConsult != -1) {
            apiService.createResponseEconsult(pref.getString(Constants.TOKEN_CONSTANT, ""), text, idEConsult).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setResponse, this::setError)

        } else {
            errorMessage.value = "Prueba de nuevo"
        }
        return responseCreated

    }

    fun setResponse(responseResponse: ResponseResponse) {
        if (responseResponse.status == Constants.HTTP_CREATED) {
            responseCreated.value = responseResponse.respuesta
        } else {
            errorMessage.value = responseResponse.message
        }
    }
}







