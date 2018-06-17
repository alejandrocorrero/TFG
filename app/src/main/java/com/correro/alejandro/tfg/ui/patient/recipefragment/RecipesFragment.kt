package com.correro.alejandro.tfg.ui.patient.recipefragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
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
import com.correro.alejandro.tfg.data.api.models.reciperesponse.Recipe
import com.correro.alejandro.tfg.databinding.FragmentRecipesItemBinding
import com.correro.alejandro.tfg.ui.patient.MainActivityPatientViewModel
import com.correro.alejandro.tfg.utils.GenericAdapter
import com.correro.alejandro.tfg.utils.error
import kotlinx.android.synthetic.main.fragment_recipes.view.*

class RecipesFragment : Fragment() {

    private lateinit var mviewmodel: MainActivityPatientViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_recipes, container, false)
        mviewmodel = ViewModelProviders.of(activity!!).get(MainActivityPatientViewModel::class.java)
        view.progressBar.visibility = View.VISIBLE
        mviewmodel.getRecipes()
        (activity as AppCompatActivity).supportActionBar!!.title="Recetas"

        mviewmodel.recipes.observe(this, Observer { setrecipe(it!!) })
        mviewmodel.errorMessage.observe(this, Observer { e -> activity!!.error(e!!, "Error"); view!!.progressBar.visibility = View.INVISIBLE })
        return view
    }

    fun setrecipe(recipe: ArrayList<Recipe>) {
        view!!.progressBar.visibility = View.INVISIBLE
        view!!.rcyRecipes.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        view!!.rcyRecipes.adapter = RecipeAdapter(recipe)
        view!!.rcyRecipes.adapter = GenericAdapter(BR.recipe,R.layout.fragment_recipes_item,null,null,recipe as java.util.ArrayList<Recipe?>,view!!.emptyRecipes)
    }


}
