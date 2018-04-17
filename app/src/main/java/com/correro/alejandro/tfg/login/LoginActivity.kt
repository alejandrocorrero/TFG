package com.correro.alejandro.tfg.login

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.correro.alejandro.tfg.R

import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mviewmodel: LoginActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mviewmodel = ViewModelProviders.of(this).get(LoginActivityViewModel::class.java)
        btnLogin.setOnClickListener{prueba()}


        /*setSupportActionBar(toolbar.findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)*/
    }

    private fun prueba() {
        mviewmodel.login(txtDni.text.toString(),txtPassword.text.toString())
        val observador = Observer<String> { nuevoNombre -> teste(nuevoNombre!!) }
        mviewmodel.token.observe(this, observador)
    }

    fun teste(test:String){
        Toast.makeText(this,test,Toast.LENGTH_LONG).show()
    }
}
