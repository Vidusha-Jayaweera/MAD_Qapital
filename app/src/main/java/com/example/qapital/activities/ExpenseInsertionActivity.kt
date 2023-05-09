package com.example.qapital.activities

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.qapital.models.ExpenseModel
import com.example.qapital.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_expense_insertion.*
import java.text.SimpleDateFormat
import java.util.*

class ExpenseInsertionActivity : AppCompatActivity() {

    private lateinit var etExpenseAmount:EditText
    private lateinit var etExpenseTitle:EditText
    private lateinit var etExpenseCategory:AutoCompleteTextView
    private lateinit var etExpenseDate:EditText
    private lateinit var etExpenseDescripton:EditText
    private lateinit var btnExpenseSave:Button

    private lateinit var dbRef:DatabaseReference

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
        etExpenseDescripton = findViewById(R.id.etExpenseDescription)
        btnExpenseSave = findViewById(R.id.expenseSaveButton)

        dbRef = FirebaseDatabase.getInstance().getReference("Expenses")

        btnExpenseSave.setOnClickListener{
            saveExpenseData()
        }

    }


    private fun saveExpenseData(){
        //getting values
        val expenseAmount = etExpenseAmount.text.toString()
        val expenseTitle = etExpenseTitle.text.toString()
        val expenseCategory = etExpenseCategory.text.toString()
        val expenseDate = etExpenseDate.text.toString()
        val expenseDescription = etExpenseDescription.text.toString()

        if(expenseAmount.isEmpty()){
            etExpenseAmount.error = "Please enter amount"
        }
        if(expenseTitle.isEmpty()){
            etExpenseTitle.error = "Please enter title"
        }
        if(expenseCategory.isEmpty()){
            etExpenseCategory.error = "Please enter category"
        }
        if(expenseDate.isEmpty()){
            etExpenseDate.error = "Please enter date"
        }
        if(expenseDescription.isEmpty()){
            etExpenseDescripton.error = "Please enter description"
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
                etExpenseDescripton.text.clear()


            }.addOnFailureListener{err->
                Toast.makeText(this,"Error ${err.message}",Toast.LENGTH_LONG).show()
            }
    }
}
