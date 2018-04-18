package com.correro.alejandro.tfg.login

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.api.models.userresponse.UserResponse
import com.correro.alejandro.tfg.medic.MainMedicActivity
import com.correro.alejandro.tfg.patient.MainActivityPatient
import com.correro.alejandro.tfg.utils.errorRequest
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mviewmodel: LoginActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mviewmodel = ViewModelProviders.of(this).get(LoginActivityViewModel::class.java)
        btnLogin.setOnClickListener { loginButton() }


        /*setSupportActionBar(toolbar.findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)*/
    }

    private fun loginButton() {
        mviewmodel.login(txtDni.text.toString(), txtPassword.text.toString())
        mviewmodel.userResponse.observe(this, Observer<UserResponse> { userresponse -> responseCall(userresponse!!) })
        mviewmodel.errorCode.observe(this, Observer<Int> { errorResponse -> errorRequest(errorResponse!!, this) })
        progressBar.visibility = View.VISIBLE
        btnLogin.isEnabled = false
    }

    private fun responseCall(userResponse: UserResponse) {
        if (userResponse.type == 1)
            startActivity(MainActivityPatient.newIntent(this, userResponse.user))
        else
            startActivity(Intent(this, MainMedicActivity::class.java))
        //Toast.makeText(this, if (userResponse.type == 1) "SOY UN PACIENTE" else "SOY UN MEDICO", Toast.LENGTH_LONG).show()


    }


}




