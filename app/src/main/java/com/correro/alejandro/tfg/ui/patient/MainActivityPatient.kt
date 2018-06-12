package com.correro.alejandro.tfg.ui.patient

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.data.api.models.chronicresponse.Chronic
import com.correro.alejandro.tfg.data.api.models.historialresponse.Historical
import com.correro.alejandro.tfg.data.api.models.userresponse.User
import com.correro.alejandro.tfg.ui.login.LoginActivity
import com.correro.alejandro.tfg.ui.patient.citatefragment.CitationFragment
import com.correro.alejandro.tfg.ui.patient.consultfragment.ConsultFragment
import com.correro.alejandro.tfg.ui.patient.patientfragment.PatientFragment
import com.correro.alejandro.tfg.ui.patient.recipefragment.RecipesFragment
import com.correro.alejandro.tfg.utils.Constants
import com.correro.alejandro.tfg.utils.Constants.Companion.PREFERENCES
import com.correro.alejandro.tfg.utils.disableShiftMode
import com.correro.alejandro.tfg.utils.executeTransaction
import kotlinx.android.synthetic.main.activity_main_patient.*

class MainActivityPatient : AppCompatActivity() {
    val FRAGMENT_PATIENT = "FRAGMENT_PATIENT"
    val FRAGMENT_CITATION = "FRAGMENT_CITATION"
    val FRAGMENT_RECIPES = "FRAGMENT_RECIPES"
    val FRAGMENT_CONSULT = "FRAGMENT_CONSULT"
    private lateinit var mviewmodel: MainActivityPatientViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_patient)
        mviewmodel = ViewModelProviders.of(this).get(MainActivityPatientViewModel::class.java)

        mviewmodel.user.value = intent.getParcelableExtra(INTENT_USER) ?: throw IllegalStateException("field $INTENT_USER missing in Intent")
        mviewmodel.historical.value = intent.getParcelableArrayListExtra<Historical>(INTENT_HISTORICAL) ?: throw IllegalStateException("field $INTENT_HISTORICAL missing in Intent")
        mviewmodel.chronics.value = intent.getParcelableArrayListExtra<Chronic>(INTENT_CHRONICS) ?: throw IllegalStateException("field $INTENT_CHRONICS missing in Intent")

        setSupportActionBar(toolbar2)
        navPatient.disableShiftMode()
        navPatient.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.mnuPatient ->{supportFragmentManager?.executeTransaction({ replace(R.id.frmMainPatient, PatientFragment(), FRAGMENT_PATIENT) }, FRAGMENT_PATIENT);mviewmodel.selectedTab=it.itemId}
                R.id.mnuRecipes -> {supportFragmentManager?.executeTransaction({ replace(R.id.frmMainPatient, RecipesFragment(), FRAGMENT_RECIPES) }, FRAGMENT_RECIPES);mviewmodel.selectedTab=it.itemId}
                R.id.mnuCitation ->{ supportFragmentManager?.executeTransaction({ replace(R.id.frmMainPatient, CitationFragment(), FRAGMENT_CITATION) }, FRAGMENT_CITATION);mviewmodel.selectedTab=it.itemId}
                R.id.mnuConsultation -> {supportFragmentManager?.executeTransaction({ replace(R.id.frmMainPatient, ConsultFragment(), FRAGMENT_CONSULT) }, FRAGMENT_CONSULT);mviewmodel.selectedTab=it.itemId}

            }
            true

        }
        navPatient.selectedItemId=mviewmodel.selectedTab
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater = MenuInflater(this)
        inflater.inflate(R.menu.main_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.mnuLogOut -> logout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout() {
        getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit().putString(Constants.TOKEN_CONSTANT, null).apply()
        getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit().putInt(Constants.TYPE_CONSTAN, 0).apply()
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        private const val INTENT_USER = "INTENT_USER"
        private const val INTENT_HISTORICAL = "INTENT_HISTORICAL"
        private const val INTENT_CHRONICS = "INTENT_CHRONICS"
        fun start(context: Context, user: User, historicals: ArrayList<Historical>, chronics: ArrayList<Chronic>) {
            val intent = Intent(context, MainActivityPatient::class.java)
            intent.putExtra(INTENT_USER, user)
            intent.putParcelableArrayListExtra(INTENT_HISTORICAL, historicals)
            intent.putParcelableArrayListExtra(INTENT_CHRONICS, chronics)
            context.startActivity(intent)
        }
    }

    override fun onBackPressed() {
        //nada
    }


}
