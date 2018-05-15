package com.correro.alejandro.tfg.ui.patient

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.net.Uri
import android.os.Environment
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
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import okio.Okio
import android.view.View
import io.reactivex.Observable
import java.io.File
import android.content.Intent
import com.correro.alejandro.tfg.data.api.models.consultslistresponse.ConsultsList
import com.correro.alejandro.tfg.data.api.models.consultslistresponse.ConsultsListResponse
import com.correro.alejandro.tfg.ui.patient.patientfragment.attachmentsfragment.FileWithType
import kotlin.concurrent.fixedRateTimer


class MainActivityPatientViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var user: User
    lateinit var historical: ArrayList<Historical>
    lateinit var chronics: ArrayList<Chronic>
    private val apiService: ApiService = ApiClient.getInstance(application.applicationContext).getService()
    lateinit var errorMessage: MutableLiveData<String>
    lateinit var recipes: MutableLiveData<ArrayList<Recipe>>
    lateinit var attchments: MutableLiveData<ArrayList<Attachment>>
    lateinit var archivo: MutableLiveData<FileWithType>
    lateinit var citatitons: MutableLiveData<ArrayList<Citation>>
    lateinit var consults: MutableLiveData<ArrayList<ConsultsList>>

    fun getConsults() {
        errorMessage = MutableLiveData()
        consults = MutableLiveData()
        apiService.getConsults(Constants.token).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setConsults, this::setError)

    }

    private fun setConsults(consultsListResponse: ConsultsListResponse) {
        if (consultsListResponse.status == Constants.HTTP_OK) {
            consults.value = consultsListResponse.consults
        }else if (consultsListResponse.status == Constants.HTTP_NOT_FOUND) {
            errorMessage.value = consultsListResponse.message
        }
    }

    fun gethistorialRecipes(id: Int) {
        recipes = MutableLiveData()
        errorMessage = MutableLiveData()
        apiService.getRecipesHistorical(Constants.token, id).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setRecipe, this::setError)
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
            is HttpException -> errorMessage.value = "Try again"
            is SocketTimeoutException -> errorMessage.value = "Try again"
            is IOException -> errorMessage.value = "IO error"
        }
    }

    fun getCitations() {
        citatitons = MutableLiveData()
        apiService.getCitationsPatient(Constants.token).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setCitations, this::setError)

    }

    private fun setCitations(citationResponse: CitationResponse) {
        if (citationResponse.status == Constants.HTTP_OK) {
            citatitons.value = citationResponse.citations
        }
    }

    fun postPrueba(file: MultipartBody.Part, name: String) {
        apiService.postTest(Constants.token, file, name).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe()
    }

    fun getRecipes() {
        recipes = MutableLiveData()
        errorMessage = MutableLiveData()
        apiService.getRecipes(Constants.token).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setRecipe, this::setError)
    }

    fun getAttchments() {
        attchments = MutableLiveData()
        errorMessage = MutableLiveData()
        apiService.getAttachments(Constants.token).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setAttachments, this::setError)
    }

    private fun setAttachments(attachmentResponse: AttachmentResponse) {
        this.attchments.value = attachmentResponse.attachments

    }

    fun downloadFile(url: String, v: View?): MutableLiveData<FileWithType> {
        if (!::archivo.isInitialized) {
            archivo = MutableLiveData()
            apiService.downloadFile(Constants.token, url).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).flatMap({ t -> test(t, v) }).subscribe(this::setfile, this::setError)
        }
        return archivo
    }

    private fun test(response: Response<ResponseBody>, v: View?): Observable<FileWithType>? {
        return Observable.create({
            val header = response.headers().get("Content-Disposition")
            val type = response.headers().get("Content-Type")
            // this is specific case, it's up to you how you want to save your file
            // if you are not downloading file from direct link, you might be lucky to obtain file name from header
            val fileName = header!!.replace("attachment; filename=", "").replace("\"", "")
            // will create file in global Music directory, can be any other directory, just don't forget to handle permissions
            v?.context?.openFileOutput(fileName, Context.MODE_PRIVATE).use { it?.write(response.body()!!.source().readByteArray()) }
            //val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absoluteFile, fileName)
            //val sink = Okio.buffer(Okio.sink(file))
            // you can access body of response
            //sink.writeAll(response.body()!!.source())
            //sink.close()
            val directory = v!!.context.filesDir
            val file = File(directory, fileName)
            it.onNext(FileWithType(file, type!!))
            it.onComplete()
        })
    }


    private fun setfile(file: FileWithType) {
        archivo.value = file
    }

}
