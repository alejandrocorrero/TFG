package com.correro.alejandro.tfg.ui.medic.consultfragment


import android.arch.lifecycle.ViewModelProviders
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.correro.alejandro.tfg.BR

import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.data.api.models.consultslistresponse.ConsultsList
import com.correro.alejandro.tfg.ui.medic.MainMedicActivityViewModel
import com.correro.alejandro.tfg.ui.patient.consultfragment.ConsultDetailActivity
import com.correro.alejandro.tfg.utils.GenericAdapter


import kotlinx.android.synthetic.main.fragment_consult_medic.view.*
import kotlinx.android.synthetic.main.fragment_patient.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ConsultFragment : Fragment() {

    private lateinit var mviewmodel: MainMedicActivityViewModel

    private lateinit var adapter: GenericAdapter<ConsultsList>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mviewmodel = ViewModelProviders.of(activity!!).get(MainMedicActivityViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_consult_medic, container, false)
        adapter = GenericAdapter(BR.consultlist, R.layout.fragment_consult_item, click() as ((ConsultsList, ViewDataBinding?) -> Unit)?)
        view.rcyConsults.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        view.rcyConsults.adapter = adapter

       view.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                when (tab!!.position) {

                    0 -> {
                        view.progressBar5.visibility = View.VISIBLE
                        mviewmodel.getConsultsPatiens()
                        mviewmodel.consults.observe(this@ConsultFragment, android.arch.lifecycle.Observer { t ->
                            adapter.newItems(t!!); view.progressBar5.visibility = View.INVISIBLE
                        })
                    }
                    1 -> {
                        view.progressBar5.visibility = View.VISIBLE
                        mviewmodel.getConsultSspecialty()
                        mviewmodel.consults.observe(this@ConsultFragment, android.arch.lifecycle.Observer { t -> adapter.newItems(t!!); view.progressBar5.visibility = View.INVISIBLE })
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {

                    0 -> {
                        view.progressBar5.visibility = View.VISIBLE
                        mviewmodel.getConsultsPatiens()
                        mviewmodel.consults.observe(this@ConsultFragment, android.arch.lifecycle.Observer { t ->
                            adapter.newItems(t!!); view.progressBar5.visibility = View.INVISIBLE
                        })
                    }
                    1 -> {
                        view.progressBar5.visibility = View.VISIBLE
                        mviewmodel.getConsultSspecialty()
                        mviewmodel.consults.observe(this@ConsultFragment, android.arch.lifecycle.Observer { t -> adapter.newItems(t!!); view.progressBar5.visibility = View.INVISIBLE })
                    }
                }


            }
        })
        view.tabLayout.getTabAt(0)!!.select()
        return view
    }

    fun click(): (ConsultsList) -> Unit {
        return {
            ConsultDetailActivity.start(activity!!, it.id.toInt())
        }

    }


}
