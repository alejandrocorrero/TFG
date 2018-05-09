package com.correro.alejandro.tfg.ui.patient

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.net.Uri
import com.correro.alejandro.tfg.data.api.ApiClient
import com.correro.alejandro.tfg.data.api.ApiService
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
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import java.net.SocketTimeoutException


class MainActivityPatientViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var user: User
    lateinit var historical: ArrayList<Historical>
    lateinit var chronics: ArrayList<Chronic>
    private val apiService: ApiService = ApiClient.getInstance(application.applicationContext).getService()
    lateinit var errorMessage: MutableLiveData<String>
    lateinit var recipes: MutableLiveData<ArrayList<Recipe>>
    lateinit var citatitons: MutableLiveData<ArrayList<Citation>>

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
    fun postPrueba(file:  MultipartBody.Part,name: String){
        apiService.postTest(Constants.token,file,name).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe()
    }

    fun getRecipes() {
        recipes = MutableLiveData()
        errorMessage = MutableLiveData()
        apiService.getRecipes(Constants.token).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::setRecipe,this::setError)
    }
}
