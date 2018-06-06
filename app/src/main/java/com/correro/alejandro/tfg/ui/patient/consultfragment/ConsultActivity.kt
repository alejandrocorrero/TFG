package com.correro.alejandro.tfg.ui.patient.consultfragment

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
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.data.api.models.specialtiesresponse.Specialty
import com.correro.alejandro.tfg.data.api.models.userresponse.User
import com.correro.alejandro.tfg.utils.createdDialog
import com.correro.alejandro.tfg.utils.error
import com.correro.alejandro.tfg.utils.permissionWrite
import kotlinx.android.synthetic.main.activity_consult_add.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ConsultActivity : AppCompatActivity() {

    private lateinit var mviewmodel: ConsultViewmodel

    private lateinit var adapter: AdapterPhotos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consult_add)
        mviewmodel = ViewModelProviders.of(this).get(ConsultViewmodel::class.java)
        progressBar13.visibility = View.VISIBLE
        mviewmodel.getSpecialties().observe(this, Observer { it -> setSpiner(it); progressBar13.visibility = View.INVISIBLE })
        mviewmodel.errorMessage.observe(this, Observer { error(it!!, "Error"); progressBar13.visibility = View.VISIBLE })
        fabAddPhoto.setOnClickListener { permissionWrite { launchCamera() } }
        mviewmodel.user = intent.getParcelableExtra(INTENT_USER) ?: throw IllegalStateException("field $INTENT_USER missing in Intent")
        setSupportActionBar(toolbar2)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);
        supportActionBar!!.setDisplayShowHomeEnabled(true);
        adapter = AdapterPhotos(click())
        photosgrid.layoutManager = GridLayoutManager(this, 2)
        photosgrid.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        photosgrid.adapter = adapter

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
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
            progressBar13.visibility = View.VISIBLE
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

            if (spinner2.selectedItemPosition == 0) {
                mviewmodel.createConsultMedic(txtConsult.text.toString(), mviewmodel.user.idMedico)
                mviewmodel.createConsult.observe(this, Observer { createConsultResponse(it) })
            } else {
                for (it: Specialty in mviewmodel.speacilties.value!!) {
                    if (it.nombre == spinner2.selectedItem) {

                        mviewmodel.createConsultSpecialty(txtConsult.text.toString(), it.id.toString())
                        mviewmodel.createConsult.observe(this, Observer {
                            createConsultResponse(it)
                        })

                        break
                    }
                }
            }
            mviewmodel.errorMessage.observe(this, Observer { error(it!!, "Warning"); progressBar13.visibility = View.INVISIBLE;window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE) })

        }
    }

    private fun createConsultResponse(it: Boolean?) {
        progressBar13.visibility = View.INVISIBLE; window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        createdDialog("Created EConsultDetail", "Success")

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
        val specialties: ArrayList<String> = ArrayList()
        specialties.add(mviewmodel.user.nombreMedico)
        for (it: Specialty in t!!) {
            specialties.add(it.nombre)
        }
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, specialties)
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

    companion object {
        private const val INTENT_USER = "INTENT_USER"
        fun start(context: Context, user: User) {
            val intent = Intent(context, ConsultActivity::class.java)
            intent.putExtra(INTENT_USER, user)
            context.startActivity(intent)
        }
    }
}
