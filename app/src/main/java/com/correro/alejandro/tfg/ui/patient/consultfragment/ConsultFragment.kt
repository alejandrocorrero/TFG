package com.correro.alejandro.tfg.ui.patient.consultfragment


import android.app.Activity
import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.ViewDataBinding
import android.os.Bundle
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
    var isLoading = false
    val visibleThreshold = 5;
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mviewmodel = ViewModelProviders.of(activity!!).get(MainActivityPatientViewModel::class.java)
        retainInstance = true

        val view = inflater.inflate(R.layout.fragment_consult, container, false)
        adapter = GenericAdapter(BR.consultlist, R.layout.fragment_consult_item, click() as ((ConsultsList, ViewDataBinding?) -> Unit)?, null, ArrayList<ConsultsList?>())

        view.rcyConsults.addItemDecoration(SimpleDividerItemDecoration(activity!!));
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

        mviewmodel.callConsults(0); view.progressBar10.visibility = View.VISIBLE
        mviewmodel.consults.observe(this, Observer { t -> adapter.newItems(t!!); view.progressBar10.visibility = View.INVISIBLE;adapter.empty=view.emptyView })
        mviewmodel.errorMessage.observe(this, Observer { t -> activity!!.error(t!!, getString(R.string.Warning_message)); view.progressBar10.visibility = View.INVISIBLE })
        view.fabAddConsult.setOnClickListener { ConsultActivity.start(activity!!, mviewmodel.user.value!!,5) }
        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.ConsultFragment_toolbar_tittle)
        return view
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 5 && resultCode == RESULT_OK){
            view!!.progressBar10.visibility = View.VISIBLE
            mviewmodel.callConsults(0)
            mviewmodel.consults.observe(this, Observer { t -> adapter.newItems(t!!); view!!.progressBar10.visibility = View.INVISIBLE})
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    private fun loadData() {
        adapter.items.add(null)
        adapter.notifyItemInserted(adapter.items.size - 1)

        mviewmodel.callConsults(adapter.items.size - 1)
        mviewmodel.consults.observe(this, Observer { t ->setValues(t!!)})
        }

    private fun setValues(data: ArrayList<ConsultsList>) {
        adapter.items.removeAt(adapter.items.size - 1)
        adapter.notifyItemRemoved(adapter.items.size - 1)
        val total = mviewmodel.maxConsults
        val start =  adapter.items.size
        val end = start + 20
        val size = if (total > end) end else total
        isLoading = total == size
        adapter.lastitems(isLoading, data)
    }

    fun click(): ((ConsultsList, FragmentConsultItemBinding) -> Unit)? {
        return { it: ConsultsList, _: FragmentConsultItemBinding? ->
            ConsultDetailActivity.start(activity!!, it.id.toInt())
        }

    }


}
