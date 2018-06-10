package com.correro.alejandro.tfg.ui.medic

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.correro.alejandro.tfg.R
import android.view.MenuInflater
import com.correro.alejandro.tfg.ui.medic.consultfragment.ConsultFragment
import com.correro.alejandro.tfg.ui.medic.diaryfragment.DiaryFragment
import com.correro.alejandro.tfg.ui.medic.econsultfragment.EConsultFragment
import com.correro.alejandro.tfg.ui.medic.searchfragment.SearchFragment
import com.correro.alejandro.tfg.utils.disableShiftMode
import com.correro.alejandro.tfg.utils.executeTransaction
import kotlinx.android.synthetic.main.activity_main_medic.*


class MainMedicActivity : AppCompatActivity() {
    val FRAGMENT_DIARY = "FRAGMENT_DIARY"
    val FRAGMENT_CONSULT = "FRAGMENT_CONSULT"
    val FRAGMENT_ECONSULT = "FRAGMENT_ECONSULT"
    val FRAGMENT_SEARCH= "FRAGMENT_SEARCH"

    private lateinit var mviewmodel: MainMedicActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_medic)
        mviewmodel = ViewModelProviders.of(this).get(MainMedicActivityViewModel::class.java)

        setSupportActionBar(toolbar2)
        navMedic.disableShiftMode()
        navMedic.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.mnuDiary -> {supportFragmentManager?.executeTransaction({ replace(R.id.frmMainMedic, DiaryFragment(), FRAGMENT_DIARY) }, FRAGMENT_DIARY);mviewmodel.selectedTab=it.itemId}
                R.id.mnuConsult -> {supportFragmentManager?.executeTransaction({ replace(R.id.frmMainMedic, ConsultFragment(), FRAGMENT_CONSULT) }, FRAGMENT_CONSULT);mviewmodel.selectedTab=it.itemId}
                R.id.mnuEConsult -> {supportFragmentManager?.executeTransaction({ replace(R.id.frmMainMedic, EConsultFragment(), FRAGMENT_ECONSULT) }, FRAGMENT_ECONSULT);mviewmodel.selectedTab=it.itemId}
                R.id.mnuSearch -> {supportFragmentManager?.executeTransaction({ replace(R.id.frmMainMedic, SearchFragment(), FRAGMENT_SEARCH) }, FRAGMENT_SEARCH);mviewmodel.selectedTab=it.itemId}
            }
            true

        }
        navMedic.selectedItemId = mviewmodel.selectedTab
    }
    override fun onBackPressed() {
        //nada
    }
}
