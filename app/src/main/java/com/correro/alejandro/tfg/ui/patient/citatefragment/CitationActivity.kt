package com.correro.alejandro.tfg.ui.patient.citatefragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.data.api.models.citationresponse.Citation
import kotlinx.android.synthetic.main.fragment_citation.*

class CitationActivity : AppCompatActivity() {

    private lateinit var mviewmodel: CitationActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_citation)
    }
}
