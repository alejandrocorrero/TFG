package com.correro.alejandro.tfg.ui.patient.consultfragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.widget.LinearLayout
import com.correro.alejandro.tfg.BR
import com.correro.alejandro.tfg.BuildConfig
import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.R.id.lblDescription
import com.correro.alejandro.tfg.data.api.models.consultpatientresponse.ConsultInfo
import com.correro.alejandro.tfg.data.api.models.consultpatientresponse.Respuesta
import com.correro.alejandro.tfg.ui.medic.searchfragment.SearchDetailActivity
import com.correro.alejandro.tfg.ui.patient.patientfragment.attachmentsfragment.FileWithType
import com.correro.alejandro.tfg.utils.GenericAdapter
import com.correro.alejandro.tfg.utils.error
import com.correro.alejandro.tfg.utils.permissionWrite
import kotlinx.android.synthetic.main.activity_consult_detail.*
import android.graphics.Paint.UNDERLINE_TEXT_FLAG
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.correro.alejandro.tfg.R.id.textView
import com.correro.alejandro.tfg.data.api.models.consultpatientresponse.Consult


class ConsultDetailActivity : AppCompatActivity() {

    private lateinit var mviewmodel: ConsultDetailActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consult_detail)
        mviewmodel = ViewModelProviders.of(this).get(ConsultDetailActivityViewModel::class.java)
        mviewmodel.idConsult = intent.getIntExtra(EXTRA_ID_CONSULT, -1)
        mviewmodel.getconsult()
        mviewmodel.consult.observe(this, Observer { initViews(it!!) })
        mviewmodel.errorMessage.observe(this, Observer { error(it!!, "Error") })


    }

    private lateinit var adapter: ConsultDetailPhotoAdapter

    private lateinit var adapterResponses: GenericAdapter<Respuesta>

    private lateinit var consultI: Consult

    private fun initViews(consultInfo: ConsultInfo) {
        var params = nstPhotos.layoutParams
        if (consultInfo.attachments.size == 0)
            params.height = 0
        nstPhotos.layoutParams = params
        adapter = ConsultDetailPhotoAdapter(setlist())
        rcyPhotos.layoutManager = GridLayoutManager(this, 2)
        rcyPhotos.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        rcyPhotos.adapter = adapter

        for (a in consultInfo.attachments)
            mviewmodel.downloadFile(a.adjunto, this).observe(this, Observer { it -> adapter.addItem(it) })
        adapterResponses = GenericAdapter(BR.response, R.layout.fragment_consult_medic_type_item, null, null, consultInfo.respuestas)
        var layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        layoutManager.reverseLayout = true
        rcyResponses.layoutManager = layoutManager
        rcyResponses.adapter = adapterResponses
        lblDescription.text = consultInfo.consult.descripcion
        if (consultInfo.consult.paciente != null) item!!.isVisible = true
        //lblNombre.paintFlags = lblNombre.paintFlags or Paint.UNDERLINE_TEXT_FLAG;
        //lblNombre.setOnClickListener { SearchDetailActivity.start(this, consultInfo.consult.idPaciente.toInt()) }
        btnSend.setOnClickListener { clickValues() }
        consultI = consultInfo.consult
    }

    private var item: MenuItem? = null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflator = menuInflater
        inflator.inflate(R.menu.consult_detail, menu)
        item = menu!!.findItem(R.id.mnuDetail)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.mnuDetail -> SearchDetailActivity.start(this, consultI.idPaciente.toInt())
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun clickValues() {
        if (!TextUtils.isEmpty(txtResponse.text)) {
            val response: String = txtResponse.text.toString()
            mviewmodel.newResponse(response).observe(this, Observer { setresult(it) })

        }
    }

    private fun setresult(it: Respuesta?) {
        adapterResponses.items.add(0, it!!)
        adapterResponses.notifyItemInserted(0)
        rcyResponses.scrollToPosition(0)

    }

    private fun setlist(): (FileWithType) -> Unit {

        return {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, it.file);
            intent.setDataAndType(uri, it.type)
            this.permissionWrite { this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1) }
        }
    }

    companion object {
        private const val EXTRA_ID_CONSULT = "EXTRA_ID_CONSULT"
        fun start(context: Context, id_consulta: Int) {
            val intent = Intent(context, ConsultDetailActivity::class.java)
            intent.putExtra(EXTRA_ID_CONSULT, id_consulta)
            context.startActivity(intent)
        }
    }
}
