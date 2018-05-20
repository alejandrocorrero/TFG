package com.correro.alejandro.tfg.ui.patient.patientfragment.historialfragment

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.correro.alejandro.tfg.BR
import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.data.api.models.historialresponse.Historical
import com.correro.alejandro.tfg.data.api.models.reciperesponse.Recipe
import com.correro.alejandro.tfg.databinding.ActivityHistorialDetailBinding
import com.correro.alejandro.tfg.utils.GenericAdapter
import kotlinx.android.synthetic.main.activity_historial_detail.*

class HistorialDetailActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityHistorialDetailBinding

    private lateinit var mviewmodel: HistoricalDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_historial_detail)
        mviewmodel = ViewModelProviders.of(this).get(HistoricalDetailViewModel::class.java)
        mviewmodel.historical = intent.getParcelableExtra(INTENT_HISTORICAL) ?: throw IllegalStateException("field $INTENT_HISTORICAL missing in Intent")
        mviewmodel.recipes = intent.getParcelableArrayListExtra<Recipe>(INTENT_RECIPES) ?: throw IllegalStateException("field $INTENT_RECIPES missing in Intent")
        mBinding.historical = mviewmodel.historical
        rcyRecipesHistorial.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        rcyRecipesHistorial.adapter = GenericAdapter(BR.recipe, R.layout.fragment_recipes_item, null, null, mviewmodel.recipes)
    }

    companion object {
        private const val INTENT_RECIPES = "INTENT_RECIPES"
        private const val INTENT_HISTORICAL = "INTENT_HISTORICAL"
        fun start(context: Context, historical: Historical, recipes: ArrayList<Recipe>) {
            val intent = Intent(context, HistorialDetailActivity::class.java)
            intent.putExtra(INTENT_HISTORICAL, historical)
            intent.putParcelableArrayListExtra(INTENT_RECIPES, recipes)
            context.startActivity(intent)
        }
    }

}
