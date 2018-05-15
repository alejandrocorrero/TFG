package com.correro.alejandro.tfg.ui.patient.consultfragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.correro.alejandro.tfg.BR

import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.data.api.models.consultslistresponse.ConsultsList
import com.correro.alejandro.tfg.ui.patient.MainActivityPatientViewModel
import com.correro.alejandro.tfg.utils.GenericAdapter
import kotlinx.android.synthetic.main.fragment_consult.view.*


class ConsultFragment : Fragment() {

    private lateinit var adapter: GenericAdapter<ConsultsList>

    private lateinit var mviewmodel: MainActivityPatientViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mviewmodel = ViewModelProviders.of(activity!!).get(MainActivityPatientViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_consult, container, false)
        adapter = GenericAdapter(BR.consultlist, R.layout.fragment_consult_item, click())
        view.rcyConsults.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        view.rcyConsults.adapter = adapter
        mviewmodel.getConsults()
        mviewmodel.consults.observe(this, Observer { t -> adapter.newItems(t!!) })
        view.fabAddConsult.setOnClickListener { ConsultActivity.start(activity!!, mviewmodel.user) }
        return view
    }

    fun click(): (ConsultsList) -> Unit {
        return {
            //mviewmodel.get
        }

    }


}
