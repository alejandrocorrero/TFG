package com.correro.alejandro.tfg.ui.patient.citatefragment


import android.app.Activity
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
import com.correro.alejandro.tfg.data.api.models.citationresponse.Citation
import com.correro.alejandro.tfg.ui.patient.MainActivityPatientViewModel
import com.correro.alejandro.tfg.utils.GenericAdapter
import kotlinx.android.synthetic.main.fragment_citation.view.*

class CitationFragment : Fragment() {

    private lateinit var mviewmodel: MainActivityPatientViewModel

    private lateinit var adapter: GenericAdapter<Citation>

    private lateinit var views: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        views = inflater.inflate(R.layout.fragment_citation, container, false)
        mviewmodel = ViewModelProviders.of(this).get(MainActivityPatientViewModel::class.java)
        callAPiCitation()
        views.fabAdd.setOnClickListener { startActivityForResult(Intent(activity, CitationActivity::class.java), 1) }
        adapter = GenericAdapter(BR.citation, R.layout.fragment_citation_item, null, null, ArrayList(), views.emptyView)
        views.rcyCitations.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        views.rcyCitations.adapter = adapter
        //validatePermissions()
        return views
    }

    private fun callAPiCitation() {
        mviewmodel.getCitations()
        views.progressBar2.visibility = View.VISIBLE
        mviewmodel.citatitons.observe(this, Observer({ setList(it) }))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK)
            callAPiCitation()
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setList(list: ArrayList<Citation>?) {
        view!!.progressBar2.visibility = View.GONE
        if (list != null) {
            adapter.newItems(list)
        }
    }


}



