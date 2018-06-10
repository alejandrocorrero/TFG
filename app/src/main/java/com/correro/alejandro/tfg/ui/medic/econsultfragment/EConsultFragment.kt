package com.correro.alejandro.tfg.ui.medic.econsultfragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.correro.alejandro.tfg.BR

import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.data.api.models.econsultresponse.Econsult
import com.correro.alejandro.tfg.databinding.FragmentEconsultItemBinding
import com.correro.alejandro.tfg.ui.medic.MainMedicActivityViewModel
import com.correro.alejandro.tfg.utils.GenericAdapter
import com.correro.alejandro.tfg.utils.error
import kotlinx.android.synthetic.main.fragment_e_consult.view.*

class EConsultFragment : Fragment() {

    private lateinit var adapter: GenericAdapter<Econsult>

    private lateinit var mviewmodel: MainMedicActivityViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mviewmodel = ViewModelProviders.of(activity!!).get(MainMedicActivityViewModel::class.java)
        (activity as AppCompatActivity).supportActionBar!!.title="Econsultas"

        val view = inflater.inflate(R.layout.fragment_e_consult, container, false)
        adapter = GenericAdapter(BR.econsult, R.layout.fragment_econsult__item, click() as ((Econsult, ViewDataBinding?) -> Unit)?)
        view.rcyEconsults.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        view.rcyEconsults.adapter = adapter
        view.fabAddEconsult.setOnClickListener { activity!!.startActivity(Intent(activity, EConsultAddActivity::class.java)) }
        view.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                view.progressBar8.visibility = View.VISIBLE
                view.rcyEconsults.visibility = View.INVISIBLE
                when (tab!!.position) {
                    0 -> mviewmodel.getEConsults()
                    1 -> mviewmodel.getEConsultSspecialty()

                }
                mviewmodel.econsults.observe(activity!!, Observer { adapter.newItems(it!!); view.progressBar8.visibility = View.INVISIBLE; view.rcyEconsults.visibility = View.VISIBLE })
                mviewmodel.errorMessage.observe(activity!!, Observer { view.progressBar8.visibility = View.INVISIBLE;activity!!.error(it!!, getString(R.string.Warning)); view.rcyEconsults.visibility = View.VISIBLE })

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                view.progressBar8.visibility = View.VISIBLE
                view.rcyEconsults.visibility = View.INVISIBLE

                when (tab!!.position) {
                    0 -> mviewmodel.getEConsults()
                    1 -> mviewmodel.getEConsultSspecialty()

                }
                mviewmodel.econsults.observe(activity!!, Observer {
                    adapter.newItems(it!!); view.progressBar8.visibility = View.INVISIBLE; view.rcyEconsults.visibility = View.VISIBLE
                })
                mviewmodel.errorMessage.observe(activity!!, Observer { view.progressBar8.visibility = View.INVISIBLE;activity!!.error(it!!, getString(R.string.Warning)); view.rcyEconsults.visibility = View.VISIBLE })

            }

        })
        view.tabLayout.getTabAt(0)!!.select()
        return view
    }

    fun click(): ((Econsult, FragmentEconsultItemBinding) -> Unit)? {
        return { it: Econsult, _: FragmentEconsultItemBinding? ->
            EConsultDetailActivity.start(activity!!, it.id.toInt())
        }

    }

}
