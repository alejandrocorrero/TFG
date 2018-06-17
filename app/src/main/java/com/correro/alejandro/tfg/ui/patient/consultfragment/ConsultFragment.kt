package com.correro.alejandro.tfg.ui.patient.consultfragment


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
import com.correro.alejandro.tfg.BR

import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.data.api.models.consultslistresponse.ConsultsList
import com.correro.alejandro.tfg.databinding.FragmentConsultItemBinding
import com.correro.alejandro.tfg.ui.patient.MainActivityPatientViewModel
import com.correro.alejandro.tfg.utils.GenericAdapter
import com.correro.alejandro.tfg.utils.SimpleDividerItemDecoration
import com.correro.alejandro.tfg.utils.error
import kotlinx.android.synthetic.main.fragment_consult.view.*


class ConsultFragment : Fragment() {

    private lateinit var adapter: GenericAdapter<ConsultsList>

    private lateinit var mviewmodel: MainActivityPatientViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mviewmodel = ViewModelProviders.of(activity!!).get(MainActivityPatientViewModel::class.java)
        retainInstance = true
        val view = inflater.inflate(R.layout.fragment_consult, container, false)
        adapter = GenericAdapter(BR.consultlist, R.layout.fragment_consult_item, click() as ((ConsultsList, ViewDataBinding?) -> Unit)?,null,ArrayList(),view.emptyView)

        view.rcyConsults.addItemDecoration(SimpleDividerItemDecoration(activity!!));
        view.rcyConsults.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        view.rcyConsults.adapter = adapter
        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.ConsultFragment_toolbar_tittle)
        mviewmodel.callConsults(); view.progressBar10.visibility = View.VISIBLE
        mviewmodel.consults.observe(this, Observer { t -> adapter.newItems(t!!); view.progressBar10.visibility = View.INVISIBLE })
        mviewmodel.errorMessage.observe(this, Observer { t -> activity!!.error(t!!, getString(R.string.Warning_message)); view.progressBar10.visibility = View.INVISIBLE })
        view.fabAddConsult.setOnClickListener { ConsultActivity.start(activity!!, mviewmodel.user.value!!) }
        return view
    }

    fun click(): ((ConsultsList, FragmentConsultItemBinding) -> Unit)? {
        return { it: ConsultsList, _: FragmentConsultItemBinding? ->
            ConsultDetailActivity.start(activity!!, it.id.toInt())
        }

    }


}
