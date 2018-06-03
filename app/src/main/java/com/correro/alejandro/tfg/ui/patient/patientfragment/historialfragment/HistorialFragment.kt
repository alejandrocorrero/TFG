package com.correro.alejandro.tfg.ui.patient.patientfragment.historialfragment


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


/**
 * A simple [Fragment] subclass.
 */
class HistorialFragment : Fragment() {


    lateinit var mviewmodel: MainActivityPatientViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mviewmodel = ViewModelProviders.of(activity!!).get(MainActivityPatientViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_historial, container, false)
        view.rcyHistoricals.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        view.rcyHistoricals.adapter = GenericAdapter(BR.historical, R.layout.fragment_historial_item, clickItem() as ((Historical, ViewDataBinding?) -> Unit)?,null, mviewmodel.historical)

        (activity as AppCompatActivity).supportActionBar!!.title="Historial"

        return view
    }

    private fun clickItem(): ((Historical, FragmentHistorialItemBinding?) -> Unit)? {
        return { it: Historical, vd: FragmentHistorialItemBinding? ->
            activity!!.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            vd!!.progresshis.visibility=View.VISIBLE
            vd.imageView13.visibility=View.INVISIBLE
            mviewmodel.gethistorialRecipes(it.id.toInt())
            mviewmodel.recipes.observe(this, Observer { d -> detail(it, d!!);vd.progresshis.visibility=View.INVISIBLE;vd.imageView13.visibility=View.VISIBLE ; activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)})
            mviewmodel.errorMessage.observe(this, Observer { b -> activity!!.error(b!!, "Alert");vd.progresshis.visibility=View.INVISIBLE;vd.imageView13.visibility=View.VISIBLE; })
        }
    }

    fun detail(historical: Historical, recipe: ArrayList<Recipe>) {
        HistorialDetailActivity.start(this.activity!!, historical, recipe)
    }

}// Required empty public constructor
