package com.correro.alejandro.tfg.ui.patient.patientfragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.SharedPreferences
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
import android.view.WindowManager
import com.correro.alejandro.tfg.R.id.tabLayout
import com.correro.alejandro.tfg.R.id.tabPacient
import com.correro.alejandro.tfg.ui.patient.MainActivityPatientViewModel
import com.correro.alejandro.tfg.ui.patient.patientfragment.attachmentsfragment.AttachmentsFragment
import com.correro.alejandro.tfg.ui.patient.patientfragment.chronicfragment.ChronicFragment
import com.correro.alejandro.tfg.ui.patient.patientfragment.historialfragment.HistorialFragment
import com.correro.alejandro.tfg.utils.Constants
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

    private lateinit var pref: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var viewgroup = inflater.inflate(R.layout.fragment_patient, container, false)
        mviewmodel = ViewModelProviders.of(activity!!).get(MainActivityPatientViewModel::class.java)
        pref = activity!!.application.getSharedPreferences(Constants.PREFERENCES, 0)!!
        if (pref.getInt(Constants.TYPE_CONSTAN, 0) == 2) {
            mviewmodel.userMedicId = arguments!!.getInt("id_user", 0)
            activity!!.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            mviewmodel.getUserMedic()
            viewgroup.progressBar9.visibility = View.VISIBLE
            mviewmodel.status.observe(this, Observer {
                activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                mviewmodel.userMedic.observe(this, Observer {
                    viewgroup.lblName.text = String.format("%s %s", it!!.nombre, it.apellido)
                    Picasso.get().load(Constants.ADDRESS+"api/web/uploads/adjuntos/" + it.foto).into(viewgroup.imgPhoto);

                    viewgroup.progressBar9.visibility = View.INVISIBLE
                })
            })
        } else {
            viewgroup.lblName.text = String.format("%s %s", mviewmodel.user.nombre, mviewmodel.user.apellido)
            Picasso.get().load(Constants.ADDRESS+"api/web/uploads/adjuntos/" + mviewmodel.user.foto).into(viewgroup.imgPhoto);
        }
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

    fun newInstance(idUser: Int): PatientFragment {
        val myFragment = PatientFragment()

        val args = Bundle()
        args.putInt("id_user", idUser)
        myFragment.arguments = args

        return myFragment
    }

}// Required empty public constructor
