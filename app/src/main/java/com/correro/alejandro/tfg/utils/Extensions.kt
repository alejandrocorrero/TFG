package com.correro.alejandro.tfg.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AlertDialog
import android.util.Log
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
fun Activity.error(message: String, tittle: String){
    AlertDialog.Builder(this).setMessage(message).setTitle(tittle).create().show()
}
fun Activity.createdDialog(message: String, tittle: String) {
    AlertDialog.Builder(this).setMessage(message).setTitle(tittle).setPositiveButton("Aceptar", { _, _ -> finish() }).setCancelable(false).create().show()
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

fun FragmentManager.executeTransaction(operations: FragmentTransaction.() -> Unit,name:String) {
    if (findFragmentByTag(name) == null) {
        val transaction = beginTransaction()
        transaction.operations()
        transaction.commit()
    }

}
