package com.correro.alejandro.tfg.ui.patient.patientfragment


import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.ui.patient.patientfragment.expedientfragment.ExpedientFragment
import com.correro.alejandro.tfg.utils.executeTransaction
import kotlinx.android.synthetic.main.fragment_patient.view.*
import android.support.design.widget.TabLayout.OnTabSelectedListener
import com.correro.alejandro.tfg.R.id.tabLayout
import com.correro.alejandro.tfg.R.id.tabPacient
import com.correro.alejandro.tfg.ui.patient.patientfragment.chronicfragment.ChronicFragment
import com.correro.alejandro.tfg.ui.patient.patientfragment.historialfragment.HistorialFragment
import kotlinx.android.synthetic.main.fragment_patient.*
import java.util.concurrent.atomic.AtomicBoolean


/**
 * A simple [Fragment] subclass.
 */
class PatientFragment : Fragment() {
    val FRAGMENT_EXPEDIENT = "FRAGMENT_EXPEDIENT"
    val FRAGMENT_HISTORIAL = "FRAGMENT_HISTORIAL"
    val FRAGMENT_CHRONIC = "FRAGMENT_CHRONIC"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.supportFragmentManager?.executeTransaction({replace(R.id.frnExpedient, ExpedientFragment(), FRAGMENT_EXPEDIENT)},FRAGMENT_EXPEDIENT)
        var viewgroup = inflater.inflate(R.layout.fragment_patient, container, false)
        viewgroup.patientTab.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab) {
                when(tab.position){
                    0->activity?.supportFragmentManager?.executeTransaction({replace(R.id.frnExpedient, ExpedientFragment(), FRAGMENT_EXPEDIENT)},FRAGMENT_EXPEDIENT)
                    1->activity?.supportFragmentManager?.executeTransaction({replace(R.id.frnExpedient, HistorialFragment(), FRAGMENT_HISTORIAL)},FRAGMENT_HISTORIAL)
                    2->activity?.supportFragmentManager?.executeTransaction({replace(R.id.frnExpedient, ChronicFragment(), FRAGMENT_CHRONIC)},FRAGMENT_CHRONIC)

                }

            }
        })

        return viewgroup
    }




}// Required empty public constructor
