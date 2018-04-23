package com.correro.alejandro.tfg.patient

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.correro.alejandro.tfg.api.models.userresponse.User


class MainActivityPatientViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var user:User
}
