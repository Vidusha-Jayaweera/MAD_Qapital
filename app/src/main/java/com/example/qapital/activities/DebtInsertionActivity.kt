package com.example.qapital.activities

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.qapital.R
import com.example.qapital.models.DebtModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class DebtInsertionActivity : AppCompatActivity() {

    lateinit var etDebtAmount : EditText
    lateinit var etDebtName : EditText
    lateinit var etDebtBorrowedDate: EditText
    lateinit var etDebtReturnDate: EditText
    private var borrowedDate: Long = 0
    private var returnDate: Long = 0
    private var date: Long = 0
    private var invertedBorrowedDate: Long = 0
    private var invertedReturnDate: Long = 0
    private var debtAmount: Double = 0.0
    lateinit var etDebtNote : EditText
    private lateinit var btnDebtInsertData : Button
    private var payStatus: String = "Not paid"

    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debt_insertion)

        //back button
        val backButton: ImageButton = findViewById(R.id.backBtn)
        backButton.setOnClickListener {
            finish()
        }

        initItem()

        //Initialize Firebase Auth and firebase database--
        val user = Firebase.auth.currentUser
        val uid = user?.uid
        if (uid != null) {
            dbRef = FirebaseDatabase.getInstance().getReference(uid) //initialize database with uid as the parent
        }
        auth = Firebase.auth

        //date picker
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

    fun clickDatePicker(editText: EditText) {
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

    fun saveDebtData(){
        //getting values
        val debtAmountEt = etDebtAmount.text.toString()
        val debtName = etDebtName.text.toString()
        val debtNote = etDebtNote.text.toString()

        if(debtAmountEt.isEmpty()){
            etDebtAmount.error = "Please enter debt amount"
        }else if(debtName.isEmpty()){
            etDebtName.error = "Please enter creditor name"
        }else{
            debtAmount = etDebtAmount.text.toString().toDouble() //convert to double type
        }

        val debtId = dbRef.push().key!!
        //convert millis value to negative, so it can be sort as descending order
        invertedBorrowedDate = borrowedDate * -1
        invertedReturnDate = returnDate * -1
        val debt = DebtModel(debtId, debtAmount, debtName, borrowedDate, returnDate, debtNote, invertedBorrowedDate, invertedReturnDate, payStatus)

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