package com.correro.alejandro.tfg.ui.patient.patientfragment.attachmentsfragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast

import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.data.api.models.attachmentsresponse.Attachment
import com.correro.alejandro.tfg.ui.patient.MainActivityPatientViewModel
import kotlinx.android.synthetic.main.fragment_attachments.view.*

class AttachmentsFragment : Fragment() {

    private lateinit var mviewmodel: MainActivityPatientViewModel

    private lateinit var adapter: AttachmentlAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_attachments, container, false)
        mviewmodel = ViewModelProviders.of(activity!!).get(MainActivityPatientViewModel::class.java)
        view.progressBar3.visibility = View.VISIBLE
        mviewmodel.getAttchments()
        adapter = AttachmentlAdapter(ArrayList(), this::click)
        view.rcyAttachment.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        view.rcyAttachment.adapter = adapter
        mviewmodel.attchments.observe(this, Observer { setRcy(it!!, view) })
        return view
    }

    private fun setRcy(list: ArrayList<Attachment>, v: View) {
        adapter.items=list
        adapter.notifyDataSetChanged()
        view!!.progressBar3.visibility = View.INVISIBLE
    }

    private fun click(it: Attachment) {
        Toast.makeText(activity, it.nombre, Toast.LENGTH_LONG).show()

    }


}
