package com.correro.alejandro.tfg.ui.login

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.data.api.models.userresponse.UserResponse
import com.correro.alejandro.tfg.ui.medic.MainMedicActivity
import com.correro.alejandro.tfg.ui.patient.MainActivityPatient
import com.correro.alejandro.tfg.utils.Constants
import com.correro.alejandro.tfg.utils.errorRequest
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mviewmodel: LoginActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mviewmodel = ViewModelProviders.of(this).get(LoginActivityViewModel::class.java)
        lblTitle.typeface = Typeface.createFromAsset(assets, "fonts/Billabong.ttf")
        var token = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE).getString(Constants.TOKEN_CONSTANT, null)
        if (token != null) {
            mviewmodel.token = token
            loginButton2()
        }
        btnLogin.setOnClickListener { loginButton() }

        //TODO TEST
        txtDni.setText("12345678M")
        txtPassword.setText("1234")

    }

    private fun loginButton() {
        val animationLogin = imageView.drawable as AnimationDrawable
        animationLogin.start()
        mviewmodel.loginCall(txtDni.text.toString(), txtPassword.text.toString())
        mviewmodel.userResponse.observe(this, Observer { it -> responseCall(it!!) })
        mviewmodel.errorCode.observe(this, Observer<Int> { errorResponse -> errorRequest(errorResponse!!);animationLogin.stop() })
        progressBar.visibility = View.VISIBLE
        btnLogin.isEnabled = false
    }

    private fun loginButton2() {
        val animationLogin = imageView.drawable as AnimationDrawable
        animationLogin.start()
        mviewmodel.loginMedicCall()
        mviewmodel.userResponse.observe(this, Observer { it -> responseCall(it!!) })
        mviewmodel.errorCode.observe(this, Observer<Int> { errorResponse -> errorRequest(errorResponse!!);animationLogin.stop() })
        progressBar.visibility = View.VISIBLE
        btnLogin.isEnabled = false
    }

    private fun responseCall(userResponse: UserResponse) {
        if (userResponse.type == 1) {
            mviewmodel.responseValues().observe(this, Observer { initPatient(it) })

        } else
            AlertDialog.Builder(this).setMessage(getString(R.string.LoginActivity_medicLogin_dialog_message)).setTitle(getString(R.string.LoginActivity_medicLogin_dialog_tittle))
                    .setPositiveButton(getString(R.string.LoginActivity_medicLogin_dialog_positiveButton), { _, _ ->
                        startActivity(Intent(this, MainMedicActivity::class.java))

                    }).setNegativeButton(getString(R.string.LoginActivity_medicLogin_dialog_negativeButton), { _, _ ->
                        mviewmodel.responseValues().observe(this, Observer { initPatient(it) })
                    }).setCancelable(false).create().show()


    }

    private fun initPatient(it: Boolean?) {
        if (it == true) {
            getSharedPreferences(Constants.PREFERENCES, 0).edit().putInt(Constants.TYPE_CONSTAN, 1).apply()
            MainActivityPatient.start(this, mviewmodel.userResponse.value!!.user, mviewmodel.historicalResponse, mviewmodel.chronicsResponse)

        }
    }

    override fun onBackPressed() {

    }
}




