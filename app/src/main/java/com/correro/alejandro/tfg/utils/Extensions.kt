package com.correro.alejandro.tfg.utils

import android.app.Activity
import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*

fun Activity.errorRequest(errorResponse: Int, contx: Context) {
    btnLogin.isEnabled = true
    progressBar.visibility = View.INVISIBLE
    when (errorResponse) {
        400 -> AlertDialog.Builder(contx).setMessage("Datos incorrectos").setTitle("Aviso").create().show()
        408 -> AlertDialog.Builder(contx).setMessage("TIMEOUT").setTitle("Aviso").create().show()
        404 -> AlertDialog.Builder(contx).setMessage("Fallo en la conexion").setTitle("Aviso").create().show()
    }
}