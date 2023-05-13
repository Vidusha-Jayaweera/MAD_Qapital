package com.example.qapital.activities

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.qapital.models.ExpenseModel
import com.example.qapital.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

import java.text.SimpleDateFormat
import java.util.*

class ExpenseInsertionActivity : AppCompatActivity() {

    private lateinit var etExpenseAmount:EditText
    private lateinit var etExpenseTitle:EditText
    private lateinit var etExpenseCategory:AutoCompleteTextView
    private lateinit var etExpenseDate:EditText
    private lateinit var etExpenseDescription:EditText
    private lateinit var btnExpenseSave:Button
    private var expenseAmount: Double = 0.0

    private lateinit var dbRef:DatabaseReference
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_insertion)

        //Back button implementation
        val backButton: ImageButton = findViewById(R.id.backBtn)
        backButton.setOnClickListener {
            finish()
        }
        //Expense categories menu dropdown
        etExpenseCategory = findViewById(R.id.etExpenseCategory)
        val listExpense = CategoryOptions.expenseCategory() //getting the arrayList data from CategoryOptions file
        val expenseAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listExpense)
        etExpenseCategory.setAdapter(expenseAdapter)

        etExpenseAmount = findViewById(R.id.etExpenseAmount)
        etExpenseTitle = findViewById(R.id.etExpenseTitle)
        etExpenseCategory = findViewById(R.id.etExpenseCategory)
        etExpenseDate = findViewById(R.id.etExpenseDate)
        etExpenseDescription = findViewById(R.id.etExpenseDescription)
        btnExpenseSave = findViewById(R.id.expenseSaveButton)

        //Initialize Firebase Auth and firebase database--
        val user = Firebase.auth.currentUser
        val uid = user?.uid
        if (uid != null) {
            dbRef = FirebaseDatabase.getInstance().getReference(uid) //initialize database with uid as the parent
        }
        auth = Firebase.auth



        btnExpenseSave.setOnClickListener{
            saveExpenseData()
        }

    }


    private fun saveExpenseData(){
        //getting values
        val expenseAmountEt = etExpenseAmount.text.toString()
        val expenseTitle = etExpenseTitle.text.toString()
        val expenseCategory = etExpenseCategory.text.toString()
        val expenseDate = etExpenseDate.text.toString()
        val expenseDescription = etExpenseDescription.text.toString()

        if(expenseAmountEt.isEmpty()){
            etExpenseAmount.error = "Please enter amount"
        }
        else if(expenseTitle.isEmpty()){
            etExpenseTitle.error = "Please enter title"
        }
        else if(expenseCategory.isEmpty()){
            etExpenseCategory.error = "Please enter category"
        }
        else if(expenseDate.isEmpty()){
            etExpenseDate.error = "Please enter date"
        }
        else if(expenseDescription.isEmpty()){
            etExpenseDescription.error = "Please enter description"
        }
        else{
            expenseAmount = etExpenseAmount.text.toString().toDouble() //convert to double type
        }
        val expenseId = dbRef.push().key!!

        val expense = ExpenseModel(expenseId,expenseAmount,expenseTitle,expenseCategory,expenseDate,expenseDescription)

        dbRef.child(expenseId).setValue(expense)
            .addOnCompleteListener{
                Toast.makeText(this,"Data inserted successfuly",Toast.LENGTH_LONG).show()

                etExpenseAmount.text.clear()
                etExpenseTitle.text.clear()
                etExpenseCategory.text.clear()
                etExpenseDate.text.clear()
                etExpenseDescription.text.clear()


            }.addOnFailureListener{err->
                Toast.makeText(this,"Error ${err.message}",Toast.LENGTH_LONG).show()
            }
    }
}
