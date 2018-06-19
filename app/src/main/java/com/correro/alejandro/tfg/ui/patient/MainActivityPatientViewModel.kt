package com.correro.alejandro.tfg.ui.patient

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.correro.alejandro.tfg.data.api.ApiClient
import com.correro.alejandro.tfg.data.api.ApiService
import com.correro.alejandro.tfg.data.api.models.attachmentsresponse.Attachment
import com.correro.alejandro.tfg.data.api.models.attachmentsresponse.AttachmentResponse
import com.correro.alejandro.tfg.data.api.models.chronicresponse.Chronic
import com.correro.alejandro.tfg.data.api.models.citationresponse.Citation
import com.correro.alejandro.tfg.data.api.models.citationresponse.CitationResponse
import com.correro.alejandro.tfg.data.api.models.historialresponse.Historical
import com.correro.alejandro.tfg.data.api.models.reciperesponse.Recipe
import com.correro.alejandro.tfg.data.api.models.reciperesponse.RecipesResponse
import com.correro.alejandro.tfg.data.api.models.userresponse.User
import com.correro.alejandro.tfg.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import android.view.View
import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.data.api.models.chronicresponse.ChronicResponse
import io.reactivex.Observable
import java.io.File
import com.correro.alejandro.tfg.data.api.models.consultslistresponse.ConsultsList
import com.correro.alejandro.tfg.data.api.models.consultslistresponse.ConsultsListResponse
import com.correro.alejandro.tfg.data.api.models.historialresponse.HistoricalResponse
import com.correro.alejandro.tfg.data.api.models.userresponse.UserResponse
import com.correro.alejandro.tfg.ui.patient.patientfragment.attachmentsfragment.FileWithType


class MainActivityPatientViewModel(application: Application) : AndroidViewModel(application) {
    var user: MutableLiveData<User> = MutableLiveData()
    var historical: MutableLiveData<ArrayList<Historical>> = MutableLiveData()
    var chronics: MutableLiveData<ArrayList<Chronic>> = MutableLiveData()

    var context = application
    private val apiService: ApiService = ApiClient.getInstance(application.applicationContext).getService()
    var pref = application.getSharedPreferences(Constants.PREFERENCES, 0)!!
    lateinit var errorMessage: MutableLiveData<String>
    lateinit var userMedic: MutableLiveData<User>
    lateinit var recipes: MutableLiveData<ArrayList<Recipe>>
    lateinit var attchments: MutableLiveData<ArrayList<Attachment>>
    lateinit var archivo: MutableLiveData<FileWithType>
    lateinit var citatitons: MutableLiveData<ArrayList<Citation>>
    lateinit var consults: MutableLiveData<ArrayList<ConsultsList>>
    lateinit var status: MutableLiveData<Boolean>
    val type = pref.getInt(Constants.TYPE_CONSTAN, 0)
    var userMedicId: Int = 0
    var selectedTab: Int = R.id.mnuPatient
    var maxConsults: Int = 0
    var maxAttachments: Int = 0
    var maxHistorical: Int = 0
    fun callConsults(position: Int) {
        errorMessage = MutableLiveData()
        consults = MutableLiveData()

        apiService.getConsults(pref.getString(Constants.TOKEN_CONSTANT, ""), position).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setConsults, this::setError)

    }

    fun callUserMedic() {
        errorMessage = MutableLiveData()
        userMedic = MutableLiveData()
        status = MutableLiveData()
        apiService.getUserMedic(pref.getString(Constants.TOKEN_CONSTANT, ""), userMedicId).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setUserResponse, this::setError)
    }

    fun callCitations() {
        citatitons = MutableLiveData()
        apiService.getCitationsPatient(pref.getString(Constants.TOKEN_CONSTANT, "")).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setCitations, this::setError)

    }

    fun callHistorialRecipes(id: Int) {
        recipes = MutableLiveData()
        errorMessage = MutableLiveData()
        if (type == 1) {
            apiService.getRecipesHistorical(pref.getString(Constants.TOKEN_CONSTANT, ""), id).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setRecipe, this::setError)
        } else {
            apiService.getRecipesHistoricalUserMedic(pref.getString(Constants.TOKEN_CONSTANT, ""), id, userMedicId).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setRecipe, this::setError)
        }
    }

    fun callAttchments(position: Int) {
        attchments = MutableLiveData()
        errorMessage = MutableLiveData()
        if (type == 1) {
            apiService.getAttachments(pref.getString(Constants.TOKEN_CONSTANT, ""), position).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setAttachments, this::setError)
        } else {
            apiService.getAttachmentsUserMedic(pref.getString(Constants.TOKEN_CONSTANT, ""), userMedicId,position).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setAttachments, this::setError)
        }
    }

    fun getRecipes() {
        recipes = MutableLiveData()
        errorMessage = MutableLiveData()
        apiService.getRecipes(pref.getString(Constants.TOKEN_CONSTANT, "")).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setRecipe, this::setError)
    }

    fun downloadFile(url: String, v: View?): MutableLiveData<FileWithType> {

        archivo = MutableLiveData()
        errorMessage = MutableLiveData()
        apiService.downloadFile(pref.getString(Constants.TOKEN_CONSTANT, ""), url).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).flatMap({ t -> downloasFileResponse(t, v) }).subscribe(this::responseFile, this::setError)

        return archivo
    }

    private fun setUserResponse(userResponse: UserResponse) {
        if (userResponse.status == Constants.HTTP_OK) {
            userMedic.value = userResponse.user
            callHistorical(0);
        }
    }

    private fun sethistorical(historicalResponse: HistoricalResponse) {
        if (historicalResponse.status == Constants.HTTP_OK) {
            historical.value = historicalResponse.dataHistorial.historicals
            maxHistorical = historicalResponse.dataHistorial.count
            apiService.getChronicsUserMedic(pref.getString(Constants.TOKEN_CONSTANT, ""), userMedicId).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setCronics, this::setError)

        }
    }

    private fun setCronics(chronicResponse: ChronicResponse) {
        if (chronicResponse.status == Constants.HTTP_OK) {
            chronics.value = chronicResponse.chronics
            status.value = true

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

    private fun setRecipe(recipesResponse: RecipesResponse) {
        if (recipesResponse.status == Constants.HTTP_OK) {
            recipes.value = recipesResponse.recipes
        } else if (recipesResponse.status == Constants.HTTP_NOT_FOUND) {
            errorMessage.value = recipesResponse.message
        }
    }

    private fun setError(e: Throwable?) {
        when (e) {
            is HttpException -> errorMessage.value = context.getString(R.string.httpException_error)
            is SocketTimeoutException -> errorMessage.value = context.getString(R.string.SocketTimeOutException)
            is IOException -> errorMessage.value = context.getString(R.string.IOException_error)
        }
    }

    private fun setCitations(citationResponse: CitationResponse) {
        if (citationResponse.status == Constants.HTTP_OK) {
            citatitons.value = citationResponse.citations
        }
    }

    private fun setAttachments(attachmentResponse: AttachmentResponse) {
        this.attchments.value = attachmentResponse.dataAttachment.rows
        this.maxAttachments = attachmentResponse.dataAttachment.count

    }

    private fun downloasFileResponse(response: Response<ResponseBody>, v: View?): Observable<FileWithType>? {
        return Observable.create({
            val header = response.headers().get("Content-Disposition")
            val type = response.headers().get("Content-Type")
            val fileName = header!!.replace("attachment; filename=", "").replace("\"", "")
            v?.context?.openFileOutput(fileName, Context.MODE_PRIVATE).use { it?.write(response.body()!!.source().readByteArray()) }
            val directory = v!!.context.filesDir
            val file = File(directory, fileName)
            it.onNext(FileWithType(file, type!!))
            it.onComplete()
        })
    }


    private fun responseFile(file: FileWithType) {
        archivo.value = file
    }

    fun callUser() {
        apiService.getUser(pref.getString(Constants.TOKEN_CONSTANT, "")).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::responseUser, this::setError)

    }

    private fun responseUser(userResponse: UserResponse) {
        user.value = userResponse.user
    }

    fun callHistorical(position: Int) {
        if (type == 1)
            apiService.getHistorical(pref.getString(Constants.TOKEN_CONSTANT, ""), position).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::responseHistoricals, this::setError)
        else
            apiService.getHistoricalUserMedic(pref.getString(Constants.TOKEN_CONSTANT, ""), userMedicId, position).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::sethistorical, this::setError)

    }

    private fun responseHistoricals(response: HistoricalResponse) {
        historical.value = response.dataHistorial.historicals
        maxHistorical = response.dataHistorial.count
    }

    fun callChronics() {
        apiService.getChronics(pref.getString(Constants.TOKEN_CONSTANT, "")).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::responseChronics, this::setError)

    }

    private fun responseChronics(response: ChronicResponse) {
        chronics.value = response.chronics

    }

}
