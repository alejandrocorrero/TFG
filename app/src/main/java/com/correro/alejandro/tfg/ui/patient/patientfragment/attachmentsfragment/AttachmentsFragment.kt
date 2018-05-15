package com.correro.alejandro.tfg.ui.patient.patientfragment.attachmentsfragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.correro.alejandro.tfg.BR
import com.correro.alejandro.tfg.BuildConfig
import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.data.api.models.attachmentsresponse.Attachment
import com.correro.alejandro.tfg.ui.patient.MainActivityPatientViewModel
import com.correro.alejandro.tfg.utils.GenericAdapter
import com.correro.alejandro.tfg.utils.permissionWrite
import kotlinx.android.synthetic.main.fragment_attachments.view.*
import java.io.File


class AttachmentsFragment : Fragment() {

    private lateinit var mviewmodel: MainActivityPatientViewModel

    private lateinit var adapter: GenericAdapter<Attachment>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_attachments, container, false)
        mviewmodel = ViewModelProviders.of(activity!!).get(MainActivityPatientViewModel::class.java)
        view.progressBar3.visibility = View.VISIBLE
        mviewmodel.getAttchments()
        adapter = GenericAdapter(BR.attachment, R.layout.fragment_attachments_item, this::click)
        view.rcyAttachment.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        view.rcyAttachment.adapter = adapter
        mviewmodel.attchments.observe(this, Observer { setRcy(it, view) })
        return view
    }

    private fun setRcy(list: ArrayList<Attachment>?, v: View) {
        if (list != null) {
            adapter.newItems(list)
        }
        view!!.progressBar3.visibility = View.INVISIBLE
    }

    private fun click(it: Attachment) {
        mviewmodel.downloadFile(it.adjunto, view).observe(this, Observer { it -> setlist(it) })
    }

    private fun setlist(it: FileWithType?) {

        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val uri =FileProvider.getUriForFile(activity!!, BuildConfig.APPLICATION_ID, it!!.file);
        intent.setDataAndType(uri, it.type)
        activity!!.permissionWrite { activity!!.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1) }

        //Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
    }


}
