package com.correro.alejandro.tfg.ui.patient.patientfragment.historialfragment

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.correro.alejandro.tfg.data.api.models.historialresponse.Historical
import com.correro.alejandro.tfg.data.api.models.reciperesponse.Recipe

class HistoricalDetailViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var historical:Historical
    lateinit var recipes:ArrayList<Recipe>
}