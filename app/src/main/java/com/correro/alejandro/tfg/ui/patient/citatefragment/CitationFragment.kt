package com.correro.alejandro.tfg.ui.patient.citatefragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.data.api.models.citationresponse.Citation
import com.correro.alejandro.tfg.ui.patient.MainActivityPatientViewModel
import kotlinx.android.synthetic.main.fragment_citation.*
import kotlinx.android.synthetic.main.fragment_citation.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class CitationFragment : Fragment() {

    private lateinit var mviewmodel: MainActivityPatientViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_citation, container, false)
        mviewmodel = ViewModelProviders.of(this).get(MainActivityPatientViewModel::class.java)
        mviewmodel.getCitations()
        mviewmodel.citatitons.observe(this, Observer(::setList))
        view.progressBar2.visibility = View.VISIBLE
        return view
    }

    private fun setList(list: ArrayList<Citation>?) {
        view!!.progressBar2.visibility = View.GONE
        view!!.rcyCitations.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        view!!.rcyCitations.adapter = CitationAdapter(list!!)
    }

}



