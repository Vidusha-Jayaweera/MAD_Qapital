package com.example.qapital

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.qapital.model.DebtModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class DebtInsertionActivity : AppCompatActivity() {

    private lateinit var etDebtAmount : EditText
    private lateinit var etDebtName : EditText
    private lateinit var etDebtBorrowedDate: EditText
    private lateinit var etDebtReturnDate: EditText
    private var borrowedDate: Long = 0
    private var returnDate: Long = 0
    private var date: Long = 0
    private lateinit var etDebtNote : EditText
    private lateinit var btnDebtInsertData : Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debt_insertion)

        initItem()

        val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val currentDate = sdf.parse(sdf.format(System.currentTimeMillis())) //take current date
        borrowedDate = currentDate!!.time //initialized date value to current date as the default value
        returnDate = currentDate!!.time //initialized date value to current date as the default value
        etDebtBorrowedDate.setOnClickListener {
            clickDatePicker(etDebtBorrowedDate)
        }
        etDebtReturnDate.setOnClickListener {
            clickDatePicker(etDebtReturnDate)
        }

        btnDebtInsertData.setOnClickListener {
            saveDebtData()
        }
    }

    private fun initItem() {
        etDebtAmount = findViewById(R.id.etDebtAmount)
        etDebtName = findViewById(R.id.etDebtName)
        etDebtBorrowedDate = findViewById(R.id.etDebtBorrowedDate)
        etDebtReturnDate = findViewById(R.id.etDebtReturnDate)
        etDebtNote = findViewById(R.id.etDebtNote)
        btnDebtInsertData = findViewById(R.id.saveButton)

        dbRef = FirebaseDatabase.getInstance().getReference("Debts")
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
                if(editText==etDebtBorrowedDate)
                    borrowedDate = theDate!!.time //convert date to millisecond
                else(editText==etDebtReturnDate)
                    returnDate = theDate!!.time //convert date to millisecond

            },
            year,
            month,
            day
        )
        dpd.show()
    }

    private fun saveDebtData(){
        //getting values
        val debtAmount = etDebtAmount.text.toString()
        val debtName = etDebtName.text.toString()
        val debtNote = etDebtNote.text.toString()

        if(debtAmount.isEmpty()){
            etDebtAmount.error = "Please enter debt amount"
        }
        if(debtName.isEmpty()){
            etDebtName.error = "Please enter creditor name"
        }
//        if(debtBorrowedDate.isEmpty()){
//            etDebtBorrowedDate.error = "Please enter debt borrowed date"
//        }
//        if(debtReturnDate.isEmpty()){
//            etDebtReturnDate.error = "Please enter debt return date"
//        }

        val debtId = dbRef.push().key!!

        val debt = DebtModel(debtId, debtAmount, debtName, borrowedDate, returnDate, debtNote)

        dbRef.child(debtId).setValue(debt)
            .addOnCompleteListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                etDebtAmount.text.clear()
                etDebtName.text.clear()
                etDebtNote.text.clear()

                etDebtBorrowedDate.text.clear()
                etDebtBorrowedDate.hint = "Borrowed date"

                etDebtReturnDate.text.clear()
                etDebtReturnDate.hint = "Return date"

            }.addOnFailureListener{ err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }
    }
}