package com.correro.alejandro.tfg.ui.medic.searchfragment

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.ui.patient.MainActivityPatient
import com.correro.alejandro.tfg.ui.patient.patientfragment.PatientFragment
import com.correro.alejandro.tfg.utils.executeTransaction
import kotlinx.android.synthetic.main.activity_search_detail.*

class SearchDetailActivity : AppCompatActivity() {
    val FRAGMENT_PATIENT = "FRAGMENT_PATIENT"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_detail)
        setSupportActionBar(toolbar2)
        var userid = intent.getIntExtra(INTENT_USER,0)
        supportFragmentManager?.executeTransaction({ replace(R.id.frgDetail, PatientFragment().newInstance(userid), FRAGMENT_PATIENT) }, FRAGMENT_PATIENT)
    }

    companion object {
        private const val INTENT_USER = "INTENT_USER"
        fun start(context: Context, user: Int) {
            val intent = Intent(context, SearchDetailActivity::class.java)
            intent.putExtra(INTENT_USER, user)
            context.startActivity(intent)
        }
    }
}
