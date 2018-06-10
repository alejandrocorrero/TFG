package com.correro.alejandro.tfg.ui.medic.econsultfragment

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.view.*
import android.widget.ArrayAdapter
import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.data.api.models.medicusersresponse.MedicUser
import com.correro.alejandro.tfg.data.api.models.specialtiesresponse.Specialty
import com.correro.alejandro.tfg.data.api.models.userresponse.User
import com.correro.alejandro.tfg.ui.patient.consultfragment.AdapterPhotos
import com.correro.alejandro.tfg.ui.patient.consultfragment.ImageItem
import com.correro.alejandro.tfg.utils.createdDialog
import com.correro.alejandro.tfg.utils.error
import com.correro.alejandro.tfg.utils.permissionWrite

import kotlinx.android.synthetic.main.activity_econsult_add.*
import kotlinx.android.synthetic.main.fragment_expedient.*
import kotlinx.android.synthetic.main.spinneradapter.*
import kotlinx.android.synthetic.main.spinneradapter.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

import android.databinding.adapters.TextViewBindingAdapter.setText
import android.widget.TextView


class EConsultAddActivity : AppCompatActivity() {

    private lateinit var mviewmodel: EConsultAddViewmodel

    private lateinit var adapter: AdapterPhotos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_econsult_add)
        setSupportActionBar(toolbar2)
        supportActionBar!!.title = "Realizar Econsulta"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);
        supportActionBar!!.setDisplayShowHomeEnabled(true);
        mviewmodel = ViewModelProviders.of(this).get(EConsultAddViewmodel::class.java)
        progressType.visibility=View.VISIBLE
        progressPatients.visibility=View.VISIBLE
        mviewmodel.getSpecialties().observe(this, Observer(this::setSpiner))
        fabAddPhotos.setOnClickListener { permissionWrite { launchCamera() } }
        mviewmodel.getUsers(null).observe(this, Observer { t -> setSpinnerPatients(t) })
        adapter = AdapterPhotos(click())
        photosGrid.layoutManager = GridLayoutManager(this, 2)
        photosGrid.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        photosGrid.adapter = adapter

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    private fun setSpinnerPatients(t: ArrayList<MedicUser>?) {
        progressPatients.visibility = View.INVISIBLE
        val aa: ArrayAdapter<MedicUser> = object : ArrayAdapter<MedicUser>(this, R.layout.spinneradapter, t) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                var view = this@EConsultAddActivity.layoutInflater;
                var convertView = view.inflate(R.layout.spinneradapter, parent, false)
                convertView!!.txtName.text = String.format("%s %s", getItem(position).nombre, getItem(position).apellido)
                return convertView
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
                return initView(position, convertView);
            }

            private fun initView(position: Int, convertView: View?): View {
                var convertView = convertView
                if (convertView == null)
                    convertView = View.inflate(context, R.layout.spinneradapter, null)
                val tvText1 = convertView!!.findViewById(R.id.txtName) as TextView
                tvText1.text = String.format("%s %s", getItem(position).nombre, getItem(position).apellido)
                return convertView
            }
        }
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner7!!.adapter = aa
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflator = menuInflater
        inflator.inflate(R.menu.consultactivitymenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.mnuAccept -> checkvalues()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun checkvalues() {
        if (TextUtils.isEmpty(txtConsult.text)) {
            error("La descripcion no puede estar vacia", "Error")
        } else {
            if (spinner2.selectedItem != null && spinner7.selectedItem != null) {
                mviewmodel.createEconsult(txtConsult.text.toString(), (spinner2.selectedItem as Specialty).id.toString(), (spinner7.selectedItem as MedicUser).id)
                mviewmodel.createEConsultResponse.observe(this, Observer { createEConsultResponse(it) })

                mviewmodel.errorMessage.observe(this, Observer { error(it!!, "Warning") })
            }else{
                error("Try again","Error")
            }
        }
    }

    private fun createEConsultResponse(it: Boolean?) {
        createdDialog("Created EConsul", "Success")

    }

    private fun click(): (ImageItem) -> Boolean {
        return {
            val position = mviewmodel.photos.indexOf(it)
            mviewmodel.photos.remove(it)
            adapter.removeItem(mviewmodel.photos, position)
            true
        }

    }

    private fun setSpiner(t: ArrayList<Specialty>?) {
        progressType.visibility = View.INVISIBLE

        val aa = object : ArrayAdapter<Specialty>(this, R.layout.spinneradapter, t) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {


                var view = this@EConsultAddActivity.layoutInflater;
                var convertView = view.inflate(R.layout.spinneradapter, parent, false)
                convertView!!.txtName.text = getItem(position).nombre
                return convertView

            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
                return initView(position, convertView);
            }

            private fun initView(position: Int, convertView: View?): View {
                var convertView = convertView
                if (convertView == null)
                    convertView = View.inflate(context, R.layout.spinneradapter, null)
                val tvText1 = convertView!!.findViewById(R.id.txtName) as TextView
                tvText1.text = getItem(position).nombre
                return convertView
            }
        }
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2!!.adapter = aa
    }

    private val TAKE_PHOTO_REQUEST = 1

    private lateinit var mCurrentPhotoPath: String

    private fun launchCamera() {
        val values = ContentValues(1)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        val fileUri = this.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(this.packageManager) != null) {
            mCurrentPhotoPath = fileUri.toString()
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(intent, TAKE_PHOTO_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                  data: Intent?) {
        if (resultCode == Activity.RESULT_OK
                && requestCode == TAKE_PHOTO_REQUEST) {
            processCapturedPhoto()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun processCapturedPhoto() {
        val cursor = this.contentResolver.query(Uri.parse(mCurrentPhotoPath), Array(1) { android.provider.MediaStore.Images.ImageColumns.DATA },
                null, null, null)
        cursor.moveToFirst()
        val photoPath = cursor.getString(0)
        cursor.close()
        val file = File(photoPath)
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        val body = MultipartBody.Part.createFormData("file", "image", reqFile);
        RequestBody.create(MediaType.parse("text/plain"), "upload_test");
        mviewmodel.photos.add(ImageItem(file, body))
        adapter.addItem(mviewmodel.photos)
        //mviewmodel.postPrueba(body, "nombre")
    }


}
