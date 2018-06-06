package com.correro.alejandro.tfg.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.Toast
import com.correro.alejandro.tfg.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

fun Activity.errorRequest(errorResponse: Int) {
    btnLogin.isEnabled = true
    progressBar.visibility = View.INVISIBLE
    when (errorResponse) {
        400 -> AlertDialog.Builder(this).setMessage("Datos incorrectos").setTitle("Aviso").create().show()
        408 -> AlertDialog.Builder(this).setMessage("TIMEOUT").setTitle("Aviso").create().show()
        404 -> AlertDialog.Builder(this).setMessage("Fallo en la conexion").setTitle("Aviso").create().show()
    }
}

fun Activity.error(message: String, tittle: String) {
    AlertDialog.Builder(this).setMessage(message).setTitle(tittle).create().show()
}

fun Activity.createdDialog(message: String, tittle: String) {
    AlertDialog.Builder(this).setMessage(message).setTitle(tittle).setPositiveButton("Aceptar", { _, _ ->
        var returnIntent = Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish()
    }).setCancelable(false).create().show()
}

@SuppressLint("RestrictedApi")
fun BottomNavigationView.disableShiftMode() {
    val menuView = getChildAt(0) as BottomNavigationMenuView
    try {
        val shiftingMode = menuView::class.java.getDeclaredField("mShiftingMode")
        shiftingMode.isAccessible = true
        shiftingMode.setBoolean(menuView, false)
        shiftingMode.isAccessible = false
        for (i in 0 until menuView.childCount) {
            val item = menuView.getChildAt(i) as BottomNavigationItemView
            item.setShiftingMode(false)
            // set once again checked value, so view will be updated
            item.setChecked(item.itemData.isChecked)
        }
    } catch (e: NoSuchFieldException) {
        Log.e(TAG, "Unable to get shift mode field", e)
    } catch (e: IllegalStateException) {
        Log.e(TAG, "Unable to change value of shift mode", e)
    }
}

fun FragmentManager.executeTransaction(operations: FragmentTransaction.() -> Unit, name: String) {
    if (findFragmentByTag(name) == null) {
        val transaction = beginTransaction()
        transaction.operations()
        transaction.commit()
    }

}

fun Activity.permissionWrite(operations: () -> Unit) {
    Dexter.withActivity(this)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    operations()
                }

                override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?,
                        token: PermissionToken?) {
                    AlertDialog.Builder(this@permissionWrite)
                            .setTitle(
                                    R.string.storage_permission_rationale_title)
                            .setMessage(
                                    R.string.storage_permition_rationale_message)
                            .setNegativeButton(
                                    android.R.string.cancel,
                                    { dialog, _ ->
                                        dialog.dismiss()
                                        token?.cancelPermissionRequest()
                                    })
                            .setPositiveButton(android.R.string.ok,
                                    { dialog, _ ->
                                        dialog.dismiss()
                                        token?.continuePermissionRequest()
                                    })
                            .setOnDismissListener({
                                token?.cancelPermissionRequest()
                            })
                            .show()
                }

                override fun onPermissionDenied(
                        response: PermissionDeniedResponse?) {
                    Log.v("Falla", "falla")
                    Toast.makeText(this@permissionWrite, "test", Toast.LENGTH_LONG).show()
                }
            })
            .check()
}

