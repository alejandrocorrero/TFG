package com.correro.alejandro.tfg.ui.patient.patientfragment


import android.arch.lifecycle.ViewModelProviders
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
import android.util.Log
import com.correro.alejandro.tfg.R.id.tabLayout
import com.correro.alejandro.tfg.R.id.tabPacient
import com.correro.alejandro.tfg.ui.patient.MainActivityPatientViewModel
import com.correro.alejandro.tfg.ui.patient.patientfragment.attachmentsfragment.AttachmentsFragment
import com.correro.alejandro.tfg.ui.patient.patientfragment.chronicfragment.ChronicFragment
import com.correro.alejandro.tfg.ui.patient.patientfragment.historialfragment.HistorialFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_patient.*
import java.util.concurrent.atomic.AtomicBoolean


/**
 * A simple [Fragment] subclass.
 */
class PatientFragment : Fragment() {
    val FRAGMENT_EXPEDIENT = "FRAGMENT_EXPEDIENT"
    val FRAGMENT_HISTORIAL = "FRAGMENT_HISTORIAL"
    val FRAGMENT_CHRONIC = "FRAGMENT_CHRONIC"
    val FRAGMENT_ATTACHMENT = "FRAGMENT_ATTACHMENT"

    private lateinit var mviewmodel: MainActivityPatientViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var viewgroup = inflater.inflate(R.layout.fragment_patient, container, false)
        mviewmodel = ViewModelProviders.of(activity!!).get(MainActivityPatientViewModel::class.java)
        viewgroup.lblName.text = String.format("%s %s", mviewmodel.user.nombre, mviewmodel.user.apellido)
        Picasso.with(context).load("http://192.168.1.213/tfgapi/api/web/uploads/adjuntos/" + mviewmodel.user.foto).into(viewgroup.imgPhoto);

        viewgroup.patientTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> activity!!.supportFragmentManager.beginTransaction().replace(R.id.frnExpedient, ExpedientFragment()).commit()
                    1 -> activity?.supportFragmentManager?.executeTransaction({ replace(R.id.frnExpedient, HistorialFragment(), FRAGMENT_HISTORIAL) }, FRAGMENT_HISTORIAL)
                    2 -> activity?.supportFragmentManager?.executeTransaction({ replace(R.id.frnExpedient, ChronicFragment(), FRAGMENT_CHRONIC) }, FRAGMENT_CHRONIC)
                    3 -> activity?.supportFragmentManager?.executeTransaction({ replace(R.id.frnExpedient, AttachmentsFragment(), FRAGMENT_ATTACHMENT) }, FRAGMENT_ATTACHMENT)

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> activity!!.supportFragmentManager.beginTransaction().replace(R.id.frnExpedient, ExpedientFragment()).commit()
                    1 -> activity?.supportFragmentManager?.executeTransaction({ replace(R.id.frnExpedient, HistorialFragment(), FRAGMENT_HISTORIAL) }, FRAGMENT_HISTORIAL)
                    2 -> activity?.supportFragmentManager?.executeTransaction({ replace(R.id.frnExpedient, ChronicFragment(), FRAGMENT_CHRONIC) }, FRAGMENT_CHRONIC)
                    3 -> activity?.supportFragmentManager?.executeTransaction({ replace(R.id.frnExpedient, AttachmentsFragment(), FRAGMENT_ATTACHMENT) }, FRAGMENT_ATTACHMENT)

                }
            }
        })
        viewgroup.patientTab.getTabAt(0)!!.select()


        //TODO REPARAR FALLO TAB
        return viewgroup
    }


}// Required empty public constructor
