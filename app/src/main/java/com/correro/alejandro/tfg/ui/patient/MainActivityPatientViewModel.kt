package com.correro.alejandro.tfg.ui.patient

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.correro.alejandro.tfg.data.api.models.historialresponse.Historical
import com.correro.alejandro.tfg.data.api.models.userresponse.User


class MainActivityPatientViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var user:User
    lateinit var historical:ArrayList<Historical>
}
