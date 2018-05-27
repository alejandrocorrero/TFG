package com.correro.alejandro.tfg.ui.patient.patientfragment.expedientfragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.databinding.FragmentExpedientBinding
import com.correro.alejandro.tfg.ui.patient.MainActivityPatientViewModel
import com.correro.alejandro.tfg.utils.Constants
import kotlinx.android.synthetic.main.fragment_expedient.view.*


class ExpedientFragment : Fragment() {

    var pref = activity!!.application.getSharedPreferences(Constants.PREFERENCES, 0)!!

    private lateinit var mviewmodel: MainActivityPatientViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentExpedientBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_expedient, container, false)
        mviewmodel = ViewModelProviders.of(activity!!).get(MainActivityPatientViewModel::class.java)
        if (pref.getInt(Constants.TYPE_CONSTAN, 0) == 2) {
            mviewmodel.userMedic.observe(this, Observer { binding.patient = it })
        } else {
            binding.patient = mviewmodel.user
        }
        return binding.root
    }

}// Required empty public constructor
