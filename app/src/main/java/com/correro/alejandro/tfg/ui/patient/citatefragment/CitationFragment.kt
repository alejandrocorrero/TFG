package com.correro.alejandro.tfg.ui.patient.citatefragment


import android.Manifest
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.correro.alejandro.tfg.BR

import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.data.api.models.citationresponse.Citation
import com.correro.alejandro.tfg.ui.patient.MainActivityPatientViewModel
import com.correro.alejandro.tfg.utils.GenericAdapter
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.fragment_citation.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class CitationFragment : Fragment() {

    private lateinit var mviewmodel: MainActivityPatientViewModel

    private lateinit var adapter: GenericAdapter<Citation>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_citation, container, false)
        mviewmodel = ViewModelProviders.of(this).get(MainActivityPatientViewModel::class.java)
        mviewmodel.getCitations()
        mviewmodel.citatitons.observe(this, Observer({ setList(it) }))
        view.progressBar2.visibility = View.VISIBLE
        view.fabAdd.setOnClickListener { startActivity(Intent(activity, CitationActivity::class.java)) }
        adapter = GenericAdapter(BR.citation, R.layout.fragment_citation_item)
        view.rcyCitations.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        view.rcyCitations.adapter = adapter
        //validatePermissions()
        return view
    }

    private fun setList(list: ArrayList<Citation>?) {
        view!!.progressBar2.visibility = View.GONE
        if (list != null) {
            adapter.newItems(list)
        }
    }


}



