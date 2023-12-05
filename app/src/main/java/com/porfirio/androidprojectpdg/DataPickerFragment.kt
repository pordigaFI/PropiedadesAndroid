package com.porfirio.androidprojectpdg

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.Calendar

class DataPickerFragment(
    val listener: (day: Int, month: Int, year: Int) -> Unit
): DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        listener(day, month, year)

    }
    override fun onCreateDialog(savedInstanteState: Bundle?): Dialog{
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val picker = DatePickerDialog(activity as Context, R.style.Theme_AndroidProjectPDG,this, year, month, day)
        //Solo se podrá agendar citas a partir de la fecha actual
        c.add(Calendar.MONTH, 0)
        picker.datePicker.minDate = c.timeInMillis
        return picker
    }

}