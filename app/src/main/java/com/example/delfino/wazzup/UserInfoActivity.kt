package com.example.delfino.wazzup

import android.annotation.TargetApi
import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.text.Editable
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_user_info.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class UserInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        ui_et_birthdate.setOnClickListener {

            hideKeyboard(ui_et_birthdate)
            selectDate(ui_et_birthdate)

        }

    }


    fun selectDate(view: View) {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


                val dpd = DatePickerDialog(this, OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                val date_default =  "${monthOfYear + 1}-$dayOfMonth-$year"


                 val inputFormatter1 : DateFormat = SimpleDateFormat("MM-dd-yy", Locale.getDefault())
                 val date_formatted : Date = inputFormatter1.parse(date_default);

                 val outputFormatter1 : DateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
                 val date_final : String = outputFormatter1.format(date_formatted);


                ui_et_birthdate.text = Editable.Factory.getInstance().newEditable(date_final)


            }, year, month, day)

            dpd.show()




      }

    fun hideKeyboard(view : View) {


        val view : View = if (currentFocus == null) View(this) else currentFocus

        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

    }


}

