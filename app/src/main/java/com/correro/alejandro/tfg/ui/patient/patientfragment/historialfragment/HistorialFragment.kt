package com.correro.alejandro.tfg.ui.patient.patientfragment.historialfragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import com.correro.alejandro.tfg.BR

import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.data.api.models.historialresponse.Historical
import com.correro.alejandro.tfg.data.api.models.reciperesponse.Recipe
import com.correro.alejandro.tfg.databinding.FragmentHistorialItemBinding
import com.correro.alejandro.tfg.ui.patient.MainActivityPatientViewModel
import com.correro.alejandro.tfg.utils.GenericAdapter
import com.correro.alejandro.tfg.utils.error
import kotlinx.android.synthetic.main.fragment_historial.view.*


class HistorialFragment : Fragment() {


    lateinit var mviewmodel: MainActivityPatientViewModel
    var isLoading = false
    val visibleThreshold = 5;
    lateinit var adapter:GenericAdapter<Historical>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mviewmodel = ViewModelProviders.of(activity!!).get(MainActivityPatientViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_historial, container, false)
        var mLayoutManager=LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        view.rcyHistoricals.layoutManager=mLayoutManager
        adapter = GenericAdapter(BR.historical, R.layout.fragment_historial_item, clickItem() as ((Historical, ViewDataBinding?) -> Unit)?, null, ArrayList<Historical?>(), view.emptyView)
        view.rcyHistoricals.adapter = adapter
        view.rcyHistoricals.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading && mLayoutManager.itemCount <= (mLayoutManager.findLastVisibleItemPosition() + visibleThreshold) && dy > 0) {
                    loadData();
                    isLoading = true;
                }
            }
        })
        mviewmodel.historical.observe(this, Observer { adapter.newItems(it!!) })
        mviewmodel.errorMessage.observe(this, Observer { b -> activity!!.error(b!!, getString(R.string.Warning_message))})
        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.HistorialFragment_toolbar_title)
        return view
    }
    private fun loadData() {
        adapter.items.add(null)
        adapter.notifyItemInserted(adapter.items.size - 1)
        mviewmodel.callHistorical(adapter.items.size - 1)
        mviewmodel.historical.observe(this, Observer { t -> setValues(t!!) })
    }

    private fun setValues(data: ArrayList<Historical>) {
        adapter.items.removeAt(adapter.items.size - 1)
        adapter.notifyItemRemoved(adapter.items.size - 1)
        val total = mviewmodel.maxHistorical
        val start = adapter.items.size
        val end = start + 20
        val size = if (total > end) end else total
        isLoading = total == size
        adapter.lastitems(isLoading, data)
    }
    private fun clickItem(): ((Historical, FragmentHistorialItemBinding?) -> Unit)? {
        return { it: Historical, vd: FragmentHistorialItemBinding? ->
            activity!!.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            vd!!.progresshis.visibility = View.VISIBLE
            vd.imageView13.visibility = View.INVISIBLE
            mviewmodel.callHistorialRecipes(it.id.toInt())
            mviewmodel.recipes.observe(this, Observer { d -> detailHistorical(it, d!!);vd.progresshis.visibility = View.INVISIBLE;vd.imageView13.visibility = View.VISIBLE; activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE) })
            mviewmodel.errorMessage.observe(this, Observer { b -> activity!!.error(b!!, getString(R.string.Warning_message));vd.progresshis.visibility = View.INVISIBLE;vd.imageView13.visibility = View.VISIBLE;activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE) })
        }
    }

    private fun detailHistorical(historical: Historical, recipe: ArrayList<Recipe>) {
        HistorialDetailActivity.start(this.activity!!, historical, recipe)
    }

    override fun onResume() {
        if (mviewmodel.type == 1) {
            mviewmodel.callUser()
            mviewmodel.callChronics()
        }
        super.onResume()
    }
}
