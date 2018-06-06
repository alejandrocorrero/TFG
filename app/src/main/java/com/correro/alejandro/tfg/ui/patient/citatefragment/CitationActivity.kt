package com.correro.alejandro.tfg.ui.patient.citatefragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.data.api.models.medichoraryresponse.Horary
import com.correro.alejandro.tfg.utils.SimpleDividerItemDecoration
import com.correro.alejandro.tfg.utils.createdDialog
import com.correro.alejandro.tfg.utils.error
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.activity_citation.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


var days = SimpleDateFormat("yyyy-MM-dd")
var time = SimpleDateFormat("HH:mm:ss")


class CitationActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        btnDay.text = SpannableStringBuilder(String.format("$dayOfMonth/%d/$year", monthOfYear + 1))
        var test = GregorianCalendar(year, monthOfYear, dayOfMonth).time
        for (i in mviewmodel.horaryMedic) {
            for (j in mviewmodel.citatitons) {
                var test2 = days.format(test)
                if (j.dia == test2) {
                    i.hours.remove(j.hora)
                }
            }


            adapter.updateItems(i.hours)

        }

    }

    private lateinit var mviewmodel: CitationActivityViewModel
    private lateinit var adapter: TimeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_citation)
        mviewmodel = ViewModelProviders.of(this).get(CitationActivityViewModel::class.java)
        mviewmodel.getCitationsMedic()
        progressBar11.visibility = View.VISIBLE
        setSupportActionBar(toolbar2)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);
        supportActionBar!!.setDisplayShowHomeEnabled(true);
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        mviewmodel.horary.observe(this, Observer { progressBar11.visibility = View.INVISIBLE; window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE) })
        adapter = TimeAdapter(ArrayList(), saveSelected())

        rcyTime.addItemDecoration(SimpleDividerItemDecoration(this));
        rcyTime.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        rcyTime.adapter = adapter
        //mviewmodel
        btnDay.setOnClickListener { v -> mviewmodel.horary.observe(this, Observer(this::setCitationsUsed)) }
        btnCreateCitation.setOnClickListener({ v ->
            v.isEnabled = false
            if (TextUtils.isEmpty(btnDay.text) || mviewmodel.selectedItem == -1) {
                error("Selecciona una fecha y hora", "Revisa datos")
                v.isEnabled = true
            } else {
                mviewmodel.createCitation(btnDay.text.toString(), adapter.items[mviewmodel.selectedItem])
                mviewmodel.errorMessage.observe(this, Observer { error(it!!, "Error");v.isEnabled = true })
                mviewmodel.citationCreated.observe(this, Observer { createdDialog("created", "Sucess");v.isEnabled = true })
            }
        })


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    private fun saveSelected(): (Int) -> Unit {
        return {
            mviewmodel.selectedItem = it
        }
    }

    private fun setCitationsUsed(t: ArrayList<Horary>?) {
        val dpd = DatePickerDialog.newInstance(
                this,
                mviewmodel.horaryMedic[0].day.get(Calendar.YEAR),
                mviewmodel.horaryMedic[0].day.get(Calendar.MONTH),
                mviewmodel.horaryMedic[0].day.get(Calendar.DAY_OF_MONTH)
        )
        var calendar = ArrayList<Calendar>()
        for (i in mviewmodel.horaryMedic) {
            calendar.add(i.day)
        }
        var test = arrayOfNulls<Calendar>(calendar.size)
        calendar.toArray(test)
        dpd.selectableDays = test
        dpd.show(fragmentManager, "Datepickerdialog")
    }
}

