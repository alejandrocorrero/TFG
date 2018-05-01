package com.correro.alejandro.tfg.ui.patient

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import android.widget.Toast
import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.data.api.models.chronicresponse.Chronic
import com.correro.alejandro.tfg.data.api.models.historialresponse.Historical
import com.correro.alejandro.tfg.data.api.models.userresponse.User
import com.correro.alejandro.tfg.ui.patient.citatefragment.CitationFragment
import com.correro.alejandro.tfg.ui.patient.patientfragment.PatientFragment
import com.correro.alejandro.tfg.utils.disableShiftMode
import com.correro.alejandro.tfg.utils.executeTransaction
import kotlinx.android.synthetic.main.activity_main_patient.*

class MainActivityPatient : AppCompatActivity() {
    val FRAGMENT_PATIENT = "FRAGMENT_PATIENT"
    val FRAGMENT_CITATION = "FRAGMENT_CITATION"
    private lateinit var mviewmodel: MainActivityPatientViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_patient)
        mviewmodel = ViewModelProviders.of(this).get(MainActivityPatientViewModel::class.java)
        mviewmodel.user = intent.getParcelableExtra(INTENT_USER) ?: throw IllegalStateException("field $INTENT_USER missing in Intent")
        mviewmodel.historical = intent.getParcelableArrayListExtra<Historical>(INTENT_HISTORICAL) ?: throw IllegalStateException("field $INTENT_HISTORICAL missing in Intent")
        mviewmodel.chronics = intent.getParcelableArrayListExtra<Chronic>(INTENT_CHRONICS) ?: throw IllegalStateException("field $INTENT_CHRONICS missing in Intent")
        //Toast.makeText(this, user.nombre, Toast.LENGTH_LONG).show()
        navPatient.disableShiftMode()
        navPatient.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.mnuPatient -> supportFragmentManager?.executeTransaction({ replace(R.id.frmMainPatient, PatientFragment(), FRAGMENT_PATIENT) }, FRAGMENT_PATIENT)
                R.id.mnuRecipes -> Toast.makeText(this, "Implementar", Toast.LENGTH_LONG).show()
                R.id.mnuCitation -> supportFragmentManager?.executeTransaction({ replace(R.id.frmMainPatient, CitationFragment(), FRAGMENT_CITATION) }, FRAGMENT_CITATION)
                R.id.mnuConsult -> Toast.makeText(this, "Implementar", Toast.LENGTH_LONG).show()
            }
            true

        }
        navPatient.selectedItemId=R.id.mnuPatient
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


}
