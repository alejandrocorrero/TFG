package com.correro.alejandro.tfg.ui.patient.patientfragment.historialfragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast

import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.data.api.models.historialresponse.Historical
import com.correro.alejandro.tfg.data.api.models.reciperesponse.Recipe
import com.correro.alejandro.tfg.ui.patient.MainActivityPatientViewModel
import com.correro.alejandro.tfg.utils.error
import kotlinx.android.synthetic.main.fragment_historial.view.*


/**
 * A simple [Fragment] subclass.
 */
class HistorialFragment : Fragment() {


    lateinit var mviewmodel: MainActivityPatientViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mviewmodel = ViewModelProviders.of(activity!!).get(MainActivityPatientViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_historial, container, false)
        view.rcyHistoricals.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        view.rcyHistoricals.adapter = HistoricalAdapter(mviewmodel.historical, clickItem())
        return view
    }

    private fun clickItem(): (Historical) -> Unit {
        return {
            mviewmodel.gethistorialRecipes(it.id.toInt())
            mviewmodel.recipes.observe(this, Observer { d -> detail(it, d!!) })
            mviewmodel.errorMessage.observe(this, Observer { b -> activity!!.error(b!!,"Alert") })
            Toast.makeText(activity, it.id, Toast.LENGTH_SHORT).show()
        }
    }

    fun detail(historical: Historical, recipe: ArrayList<Recipe>) {
        HistorialDetailActivity.start(this.activity!!, historical, recipe)
    }

}// Required empty public constructor
