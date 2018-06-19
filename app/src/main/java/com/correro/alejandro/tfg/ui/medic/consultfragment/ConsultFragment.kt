package com.correro.alejandro.tfg.ui.medic.consultfragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.correro.alejandro.tfg.BR

import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.data.api.models.consultslistresponse.ConsultsList
import com.correro.alejandro.tfg.databinding.FragmentConsultItemBinding
import com.correro.alejandro.tfg.ui.medic.MainMedicActivityViewModel
import com.correro.alejandro.tfg.ui.patient.consultfragment.ConsultDetailActivity
import com.correro.alejandro.tfg.utils.GenericAdapter
import kotlinx.android.synthetic.main.fragment_consult_medic.*


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
    var isLoading = false
    val visibleThreshold = 5;
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mviewmodel = ViewModelProviders.of(activity!!).get(MainMedicActivityViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_consult_medic, container, false)
        adapter = GenericAdapter(BR.consultlist, R.layout.fragment_consult_item, click() as ((ConsultsList, ViewDataBinding?) -> Unit)?)
        var mLayoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        view.rcyConsults.layoutManager = mLayoutManager
        view.rcyConsults.adapter = adapter
        view.rcyConsults.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading && mLayoutManager.itemCount <= (mLayoutManager.findLastVisibleItemPosition() + visibleThreshold) && dy > 0) {
                    loadData();
                    isLoading = true;
                }
            }
        })
        (activity as AppCompatActivity).supportActionBar!!.title = "Consultas"

        view.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabReselected(tab: TabLayout.Tab?) {
                tabPos(tab, view)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tabPos(tab, view)


            }
        })
        view.tabLayout.getTabAt(0)!!.select()
        return view
    }

    private fun loadData() {
        adapter.items.add(null)
        rcyConsults.post { adapter.notifyItemInserted(adapter.items.size - 1) }
        when (tabLayout.selectedTabPosition) {
            0 -> {
                mviewmodel.getConsultsPatiens(adapter.items.size - 1)
            }
            1->{
                mviewmodel.getConsultSspecialty(adapter.items.size - 1)

            }
        }
        mviewmodel.consults.observe(this, Observer { t -> setValues(t!!) })
    }

    private fun setValues(data: ArrayList<ConsultsList>) {
        adapter.items.removeAt(adapter.items.size - 1)
        adapter.notifyItemRemoved(adapter.items.size - 1)
        val total = mviewmodel.maxConsults
        val start = adapter.items.size
        val end = start + 20
        val size = if (total > end) end else total
        isLoading = total == size
        adapter.lastitems(isLoading, data)
    }

    private fun tabPos(tab: TabLayout.Tab?, view: View) {
        isLoading = false
        view.rcyConsults.visibility=View.INVISIBLE

        when (tab!!.position) {

            0 -> {
                view.progressBar5.visibility = View.VISIBLE
                mviewmodel.getConsultsPatiens(0)
                mviewmodel.consults.observe(this@ConsultFragment, Observer { t ->
                    adapter.newItems(t!!); view.progressBar5.visibility = View.INVISIBLE
                    view.rcyConsults.visibility=View.VISIBLE

                })
            }
            1 -> {
                view.progressBar5.visibility = View.VISIBLE
                mviewmodel.getConsultSspecialty(0)
                mviewmodel.consults.observe(this@ConsultFragment, Observer { t -> adapter.newItems(t!!); view.progressBar5.visibility = View.INVISIBLE;        view.rcyConsults.visibility=View.VISIBLE})
            }
        }
    }

    fun click(): ((ConsultsList, FragmentConsultItemBinding) -> Unit)? {
        return { it: ConsultsList, _: FragmentConsultItemBinding? ->
            ConsultDetailActivity.start(activity!!, it.id.toInt())
        }

    }


}
