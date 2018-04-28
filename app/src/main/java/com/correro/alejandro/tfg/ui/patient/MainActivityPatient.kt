package com.correro.alejandro.tfg.ui.patient

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.data.api.models.historialresponse.Historical
import com.correro.alejandro.tfg.data.api.models.userresponse.User
import com.correro.alejandro.tfg.ui.patient.patientfragment.PatientFragment
import com.correro.alejandro.tfg.utils.disableShiftMode
import com.correro.alejandro.tfg.utils.executeTransaction
import kotlinx.android.synthetic.main.activity_main_patient.*

class MainActivityPatient : AppCompatActivity() {
    val FRAGMENT_PATIENT = "FRAGMENT_PATIENT"
    private lateinit var mviewmodel: MainActivityPatientViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_patient)
        mviewmodel = ViewModelProviders.of(this).get(MainActivityPatientViewModel::class.java)
        mviewmodel.user = intent.getParcelableExtra(INTENT_USER) ?: throw IllegalStateException("field $INTENT_USER missing in Intent")
        mviewmodel.historical = intent.getParcelableArrayListExtra<Historical>(INTENT_HISTORICAL) ?: throw IllegalStateException("field $INTENT_HISTORICAL missing in Intent")
        //Toast.makeText(this, user.nombre, Toast.LENGTH_LONG).show()
        navPatient.disableShiftMode()
        supportFragmentManager.executeTransaction({ replace(R.id.frmMainPatient, PatientFragment(), FRAGMENT_PATIENT) }, FRAGMENT_PATIENT)
    }

    companion object {
        private const val INTENT_USER = "INTENT_USER"
        private const val INTENT_HISTORICAL = "INTENT_HISTORICAL"
        fun newIntent(context: Context, user: User, historicals: ArrayList<Historical>): Intent {
            val intent = Intent(context, MainActivityPatient::class.java)
            intent.putExtra(INTENT_USER, user)
            intent.putParcelableArrayListExtra(INTENT_HISTORICAL, historicals)
            return intent
        }
    }


}
