package com.correro.alejandro.tfg.ui.patient.patientfragment.chronicfragment


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.correro.alejandro.tfg.BR

import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.data.api.models.chronicresponse.Chronic
import com.correro.alejandro.tfg.data.api.models.historialresponse.Historical
import com.correro.alejandro.tfg.ui.patient.MainActivityPatientViewModel
import com.correro.alejandro.tfg.ui.patient.patientfragment.historialfragment.HistoricalAdapter
import com.correro.alejandro.tfg.utils.GenericAdapter
import kotlinx.android.synthetic.main.fragment_chronic.view.*
import kotlinx.android.synthetic.main.fragment_historial.view.*

class ChronicFragment : Fragment() {

    private lateinit var mviewmodel: MainActivityPatientViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mviewmodel = ViewModelProviders.of(activity!!).get(MainActivityPatientViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_chronic, container, false)
        view.rcyChronics.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        view.rcyChronics.adapter=GenericAdapter(BR.chronic,R.layout.fragment_chronic_item,null,null,mviewmodel.chronics)
        return view
    }
}



