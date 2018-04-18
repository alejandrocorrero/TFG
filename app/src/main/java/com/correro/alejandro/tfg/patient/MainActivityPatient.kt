package com.correro.alejandro.tfg.patient

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.api.models.userresponse.User

class MainActivityPatient : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_patient)
        val user: User = intent.getParcelableExtra(INTENT_USER) ?: throw IllegalStateException("field $INTENT_USER missing in Intent")
        Toast.makeText(this, user.nombre, Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val INTENT_USER = "INTENT_USER"
        fun newIntent(context: Context, user: User): Intent {
            val intent = Intent(context, MainActivityPatient::class.java)
            intent.putExtra(INTENT_USER, user)
            return intent
        }
    }

}
