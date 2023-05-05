package com.example.qapital.activities

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.qapital.R
import java.text.SimpleDateFormat
import java.util.*

class DebtUpdateActivity : AppCompatActivity() {

    private lateinit var etBorrowedDate: EditText
    private lateinit var etReturnDate: EditText
    private var date: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debt_update)

        initItem()

        val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val currentDate = sdf.parse(sdf.format(System.currentTimeMillis())) //take current date
        date = currentDate!!.time //initialized date value to current date as the default value
        etBorrowedDate.setOnClickListener {
            clickDatePicker(etBorrowedDate)
        }
        etReturnDate.setOnClickListener {
            clickDatePicker(etReturnDate)
        }
    }

    private fun initItem() {
        etBorrowedDate = findViewById(R.id.borrowedDateUpdate)
        etReturnDate = findViewById(R.id.returnDateUpdate)
    }

    private fun clickDatePicker(editText: EditText) {
        val myCalendar = Calendar.getInstance()
        val year = myCalendar.get(Calendar.YEAR)
        val month = myCalendar.get(Calendar.MONTH)
        val day = myCalendar.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->

                val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                editText.text = null
                editText.hint = selectedDate

                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                val theDate = sdf.parse(selectedDate)
                date = theDate!!.time //convert date to millisecond

            },
            year,
            month,
            day
        )
        dpd.show()
    }
}