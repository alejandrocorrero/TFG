package com.correro.alejandro.tfg.ui.patient.patientfragment.expedientfragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.databinding.FragmentExpedientBinding
import com.correro.alejandro.tfg.ui.patient.MainActivityPatientViewModel
import com.correro.alejandro.tfg.utils.Constants
import kotlinx.android.synthetic.main.fragment_expedient.view.*


class ExpedientFragment : Fragment() {



    private lateinit var mviewmodel: MainActivityPatientViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentExpedientBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_expedient, container, false)
        mviewmodel = ViewModelProviders.of(activity!!).get(MainActivityPatientViewModel::class.java)
        val pref = activity!!.application.getSharedPreferences(Constants.PREFERENCES, 0)!!
        if (pref.getInt(Constants.TYPE_CONSTAN, 0) == 2) {
            mviewmodel.userMedic.observe(this, Observer { binding.patient = it })
        } else {
            mviewmodel.user.observe(this,Observer { binding.patient = it })
        }
        (activity!! as AppCompatActivity).supportActionBar!!.title=getString(R.string.ExpedientFragment_toolbar_tittle)

        return binding.root
    }
    override fun onResume() {
        mviewmodel.callHistorical()
        mviewmodel.callChronics()

        super.onResume()
    }
}
