package com.correro.alejandro.tfg.patient.patientfragment.expedientfragment


import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.databinding.FragmentExpedientBinding
import com.correro.alejandro.tfg.patient.MainActivityPatientViewModel
import kotlinx.android.synthetic.main.fragment_expedient.view.*


class ExpedientFragment : Fragment() {


    private lateinit var mviewmodel: MainActivityPatientViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentExpedientBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_expedient, container, false)
        val view = binding.root
        mviewmodel = ViewModelProviders.of(activity!!).get(MainActivityPatientViewModel::class.java)
        binding.patient = mviewmodel.user
        return view
    }

}// Required empty public constructor
