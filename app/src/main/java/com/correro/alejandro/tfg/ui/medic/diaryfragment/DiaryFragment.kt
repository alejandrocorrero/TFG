package com.correro.alejandro.tfg.ui.medic.diaryfragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.correro.alejandro.tfg.BR

import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.data.api.models.citationresponse.Citation
import com.correro.alejandro.tfg.databinding.FragmentCitationItemBinding
import com.correro.alejandro.tfg.databinding.FragmentDiaryItemBinding
import com.correro.alejandro.tfg.ui.medic.MainMedicActivityViewModel
import com.correro.alejandro.tfg.ui.medic.searchfragment.SearchDetailActivity
import com.correro.alejandro.tfg.ui.patient.patientfragment.PatientFragment
import com.correro.alejandro.tfg.utils.GenericAdapter
import kotlinx.android.synthetic.main.fragment_citation.view.*
import kotlinx.android.synthetic.main.fragment_citation_item.*

class DiaryFragment : Fragment() {

    private lateinit var mviewmodel: MainMedicActivityViewModel


    private lateinit var adapter: GenericAdapter<Citation>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_citation, container, false)
        mviewmodel = ViewModelProviders.of(this).get(MainMedicActivityViewModel::class.java)
        mviewmodel.getCitations()
        mviewmodel.citatitons.observe(this, Observer({ setList(it) }))
        view.progressBar2.visibility = View.VISIBLE
        view.fabAdd.visibility=View.INVISIBLE
        (activity as AppCompatActivity).supportActionBar!!.title="Agenda para hoy"

        adapter = GenericAdapter(BR.citation, R.layout.fragment_diary_item, click() as ((Citation, ViewDataBinding?) -> Unit)?,null,ArrayList(),view.emptyView)
        view.rcyCitations.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        view.rcyCitations.adapter = adapter
        return view
    }

    private fun click(): ((Citation,FragmentDiaryItemBinding?) -> Unit)? {
        return { it: Citation, vd: FragmentDiaryItemBinding? ->
            SearchDetailActivity.start(activity!!,it.idPaciente.toInt())
        }
    }

    private fun setList(list: ArrayList<Citation>?) {
        view!!.progressBar2.visibility = View.GONE
        if (list != null) {
            adapter.newItems(list)
        }
    }


}


