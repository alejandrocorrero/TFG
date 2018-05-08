package com.correro.alejandro.tfg.ui.patient.citatefragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.data.api.models.citattionsmedicresponse.CitationMedicUsed
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CitationActivity : AppCompatActivity() {

    private lateinit var mviewmodel: CitationActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_citation)
        mviewmodel = ViewModelProviders.of(this).get(CitationActivityViewModel::class.java)
        mviewmodel.getCitationsMedic()
        //mviewmodel

        //mviewmodel.citatitons.observe(this, Observer(this::setCitationsUsed))

    }

    private fun setCitationsUsed(t: ArrayList<CitationMedicUsed>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

