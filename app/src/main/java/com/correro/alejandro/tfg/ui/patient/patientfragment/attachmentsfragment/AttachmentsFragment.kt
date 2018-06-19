package com.correro.alejandro.tfg.ui.patient.patientfragment.attachmentsfragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
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
import com.correro.alejandro.tfg.databinding.FragmentAttachmentsItemBinding
import com.correro.alejandro.tfg.ui.patient.MainActivityPatientViewModel
import com.correro.alejandro.tfg.utils.GenericAdapter
import com.correro.alejandro.tfg.utils.error
import com.correro.alejandro.tfg.utils.permissionWrite
import kotlinx.android.synthetic.main.fragment_attachments.view.*


class AttachmentsFragment : Fragment() {

    private lateinit var mviewmodel: MainActivityPatientViewModel

    private lateinit var adapter: GenericAdapter<Attachment>
    var isLoading = false
    val visibleThreshold = 5;
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_attachments, container, false)
        mviewmodel = ViewModelProviders.of(activity!!).get(MainActivityPatientViewModel::class.java)
        view.progressBar3.visibility = View.VISIBLE
        adapter = GenericAdapter(BR.attachment, R.layout.fragment_attachments_item, click() as ((Attachment, ViewDataBinding?) -> Unit)?, null, ArrayList<Attachment?>())
        var mLayoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        view.rcyAttachment.layoutManager = mLayoutManager
        view.rcyAttachment.adapter = adapter
        mviewmodel.callAttchments(0)

        view.rcyAttachment.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading && mLayoutManager.itemCount <= (mLayoutManager.findLastVisibleItemPosition() + visibleThreshold) && dy > 0) {
                    loadData();
                    isLoading = true;
                }
            }
        })
        mviewmodel.attchments.observe(this, Observer { setRcy(it, view) })
        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.AttachmentsFragment_toolbar_tittle)

        return view
    }

    private fun loadData() {
        adapter.items.add(null)
        adapter.notifyItemInserted(adapter.items.size - 1)

        mviewmodel.callAttchments(adapter.items.size - 1)
        mviewmodel.attchments.observe(this, Observer { t -> setValues(t!!) })
    }

    private fun setValues(data: ArrayList<Attachment>) {
        adapter.items.removeAt(adapter.items.size - 1)
        adapter.notifyItemRemoved(adapter.items.size - 1)
        val total = mviewmodel.maxAttachments
        val start = adapter.items.size
        val end = start + 20
        val size = if (total > end) end else total
        isLoading = total == size
        adapter.lastitems(isLoading, data)
    }

    private fun setRcy(list: ArrayList<Attachment>?, v: View) {
        adapter.empty = v.emptyView


        if (list != null) {
            adapter.newItems(list)
        }
        view!!.progressBar3.visibility = View.INVISIBLE
    }

    private fun click(): (Attachment, FragmentAttachmentsItemBinding) -> Unit {
        return { it: Attachment, b: FragmentAttachmentsItemBinding? ->
            b!!.progressAttachment.visibility = View.VISIBLE
            mviewmodel.downloadFile(it.adjunto, view).observe(this, Observer { it2 -> openFile(it2); b.progressAttachment.visibility = View.INVISIBLE })
            mviewmodel.errorMessage.observe(this, Observer { it2 -> activity!!.error(it2!!, getString(R.string.Warning_message)); b.progressAttachment.visibility = View.INVISIBLE })
        }

    }

    private fun openFile(it: FileWithType?) {

        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val uri = FileProvider.getUriForFile(activity!!, BuildConfig.APPLICATION_ID, it!!.file);
        intent.setDataAndType(uri, it.type)
        activity!!.permissionWrite { activity!!.startActivityForResult(Intent.createChooser(intent, getString(R.string.AttachmentsFragment_photo)), 1) }
    }

    override fun onResume() {
        if (mviewmodel.type == 1) {

            mviewmodel.callUser()
            mviewmodel.callChronics()
            mviewmodel.callHistorical(0)
        }
        super.onResume()
    }
}
