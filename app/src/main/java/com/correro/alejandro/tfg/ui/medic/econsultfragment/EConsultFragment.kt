package com.correro.alejandro.tfg.ui.medic.econsultfragment


import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
import kotlinx.android.synthetic.main.fragment_e_consult.*
import kotlinx.android.synthetic.main.fragment_e_consult.view.*

class EConsultFragment : Fragment() {

    private lateinit var adapter: GenericAdapter<Econsult>
    var isLoading = false
    val visibleThreshold = 5;
    private lateinit var mviewmodel: MainMedicActivityViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mviewmodel = ViewModelProviders.of(activity!!).get(MainMedicActivityViewModel::class.java)
        (activity as AppCompatActivity).supportActionBar!!.title="Econsultas"

        val view = inflater.inflate(R.layout.fragment_e_consult, container, false)
        adapter = GenericAdapter(BR.econsult, R.layout.fragment_econsult__item, click() as ((Econsult, ViewDataBinding?) -> Unit)?)
        var mLayoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        view.rcyEconsults.layoutManager = mLayoutManager
        view.rcyEconsults.adapter = adapter
        view.rcyEconsults.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading && mLayoutManager.itemCount <= (mLayoutManager.findLastVisibleItemPosition() + visibleThreshold) && dy > 0) {
                    loadData();
                    isLoading = true;
                }
            }
        })
        view.fabAddEconsult.setOnClickListener { startActivityForResult(Intent(activity, EConsultAddActivity::class.java),10) }
        view.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                view.progressBar8.visibility = View.VISIBLE
                view.rcyEconsults.visibility = View.INVISIBLE
                when (tab!!.position) {
                    0 -> mviewmodel.getEConsults(0)
                    1 -> mviewmodel.getEConsultSspecialty(0)

                }
                mviewmodel.econsults.observe(activity!!, Observer { adapter.newItems(it!!); view.progressBar8.visibility = View.INVISIBLE; view.rcyEconsults.visibility = View.VISIBLE })
                mviewmodel.errorMessage.observe(activity!!, Observer { view.progressBar8.visibility = View.INVISIBLE;activity!!.error(it!!, getString(R.string.Warning_message)); view.rcyEconsults.visibility = View.VISIBLE })

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                view.progressBar8.visibility = View.VISIBLE
                view.rcyEconsults.visibility = View.INVISIBLE

                when (tab!!.position) {
                    0 -> mviewmodel.getEConsults(0)
                    1 -> mviewmodel.getEConsultSspecialty(0)

                }
                mviewmodel.econsults.observe(activity!!, Observer {
                    adapter.newItems(it!!); view.progressBar8.visibility = View.INVISIBLE; view.rcyEconsults.visibility = View.VISIBLE
                })
                mviewmodel.errorMessage.observe(activity!!, Observer { view.progressBar8.visibility = View.INVISIBLE;activity!!.error(it!!, getString(R.string.Warning_message)); view.rcyEconsults.visibility = View.VISIBLE })

            }

        })
        view.tabLayout.getTabAt(0)!!.select()
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode==Activity.RESULT_OK){
            view!!.progressBar8.visibility = View.VISIBLE
            when (tabLayout.selectedTabPosition) {
                0 -> {
                    mviewmodel.getEConsults(0)
                }
                1->{
                    mviewmodel.getEConsultSspecialty(0)

                }
            }

            mviewmodel.econsults.observe(activity!!, Observer { adapter.newItems(it!!); view!!.progressBar8.visibility = View.INVISIBLE; view!!.rcyEconsults.visibility = View.VISIBLE })

        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    private fun loadData() {
        adapter.items.add(null)
        rcyEconsults.post { adapter.notifyItemInserted(adapter.items.size - 1) }
        when (tabLayout.selectedTabPosition) {
            0 -> {
                mviewmodel.getEConsults(adapter.items.size - 1)
            }
            1->{
                mviewmodel.getEConsultSspecialty(adapter.items.size - 1)

            }
        }
        mviewmodel.econsults.observe(this, Observer { t -> setValues(t!!) })
    }

    private fun setValues(data: ArrayList<Econsult>) {
        adapter.items.removeAt(adapter.items.size - 1)
        adapter.notifyItemRemoved(adapter.items.size - 1)
        val total = mviewmodel.maxEConsults
        val start = adapter.items.size
        val end = start + 20
        val size = if (total > end) end else total
        isLoading = total == size
        adapter.lastitems(isLoading, data)
    }
    fun click(): ((Econsult, FragmentEconsultItemBinding) -> Unit)? {
        return { it: Econsult, _: FragmentEconsultItemBinding? ->
            EConsultDetailActivity.start(activity!!, it.id.toInt())
        }

    }

}
