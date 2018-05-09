package com.correro.alejandro.tfg.ui.patient.citatefragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.SpannableStringBuilder
import android.widget.Toast
import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.data.api.models.citattionsmedicresponse.CitationMedicUsed
import com.correro.alejandro.tfg.data.api.models.medichoraryresponse.Horary
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.readystatesoftware.chuck.internal.ui.MainActivity
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.activity_citation.*
import android.R.attr.data
import android.widget.ArrayAdapter
import java.time.LocalDate


var days = SimpleDateFormat("yyyy-MM-dd")


class CitationActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    override fun onTimeSet(view: TimePickerDialog?, hourOfDay: Int, minute: Int, second: Int) {

    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        txtDay.text = SpannableStringBuilder(String.format("$dayOfMonth/%d/$year",monthOfYear+1))
        var test=GregorianCalendar(year,monthOfYear,dayOfMonth).time
        for (i in mviewmodel.horaryMedic) {
            for (j in mviewmodel.citatitons) {
                var test2=days.format(test)
                if (j.dia == test2) {
                    i.hours.remove(j.hora)
                }
            }

            val itemsAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, i.hours)
            itemsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = itemsAdapter

        }

    }

    private lateinit var mviewmodel: CitationActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_citation)
        mviewmodel = ViewModelProviders.of(this).get(CitationActivityViewModel::class.java)
        mviewmodel.getCitationsMedic()
        //mviewmodel
        imageButton2.setOnClickListener { v -> mviewmodel.horary.observe(this, Observer(this::setCitationsUsed)) }


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

