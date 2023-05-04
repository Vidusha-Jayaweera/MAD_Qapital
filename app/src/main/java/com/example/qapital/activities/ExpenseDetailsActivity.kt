package com.example.qapital.activities

import android.content.Intent
import android.icu.util.CurrencyAmount
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.qapital.R
import com.example.qapital.models.ExpenseModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_expense_details.*

class ExpenseDetailsActivity : AppCompatActivity() {
    private lateinit var tvExpenseDate:TextView
    private lateinit var tvExpenseAmount:TextView
    private lateinit var tvExpenseTitle:TextView
    private lateinit var tvExpenseCategory:TextView
    private lateinit var tvExpenseNote:TextView
    private lateinit var btnUpdate:ImageButton
    private lateinit var btnDelete:ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_details)
        tvExpenseDate = findViewById(R.id.tvExpenseDateDetails)
        tvExpenseAmount= findViewById(R.id.tvExpenseAmountDetails)
        tvExpenseTitle= findViewById(R.id.tvExpenseTitleDetails)
        tvExpenseCategory = findViewById(R.id.tvExpenseCategoryDetails)
        tvExpenseNote = findViewById(R.id.tvExpenseNoteDetails)
        btnUpdate = findViewById(R.id.updateExpenseData)
        btnDelete = findViewById(R.id.deleteExpenseData)
        initView()
        setValuesToViews()
        btnUpdate.setOnClickListener{
            openUpdateDialog(
                intent.getStringExtra("expenseId").toString(),
                intent.getStringExtra("expenseTitle").toString()
            )
        }
        btnDelete.setOnClickListener{
            deleteRecord(
                intent.getStringExtra("expenseId").toString()
            )
        }
    }
    private fun deleteRecord(
        id:String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Expenses").child(id)
        val eTask = dbRef.removeValue()

        eTask.addOnSuccessListener {
            Toast.makeText(this,"Expense data deleted",Toast.LENGTH_LONG).show()

            val intent = Intent(this,ExpenseFetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{error ->
            Toast.makeText(this,"Deleting Err ${error.message}",Toast.LENGTH_LONG).show()
        }
    }
    private fun initView(){}
    private fun setValuesToViews(){
        tvExpenseDate.text = intent.getStringExtra("expenseDate")
        tvExpenseAmount.text = intent.getStringExtra("expenseAmount")
        tvExpenseCategory.text = intent.getStringExtra("expenseCategory")
        tvExpenseTitle.text = intent.getStringExtra("expenseTitle")
        tvExpenseNote.text = intent.getStringExtra("expenseDescription")
    }
    private fun openUpdateDialog(
        expenseId:String,
        expenseTitle:String
    ){
        val eDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val eDialogView = inflater.inflate(R.layout.activity_expense_update,null)

        eDialog.setView(eDialogView)
        val etExpenseAmount = eDialogView.findViewById<EditText>(R.id.etExpenseAmount)
        val etExpenseTitle = eDialogView.findViewById<EditText>(R.id.etExpenseTitle)
        val etExpenseCategory = eDialogView.findViewById<EditText>(R.id.etExpenseCategory)
        val etExpenseDate = eDialogView.findViewById<EditText>(R.id.etExpenseDate)
        val etExpenseDescription = eDialogView.findViewById<EditText>(R.id.etExpenseDescription)
        val btnExpenseUpdate = eDialogView.findViewById<Button>(R.id.expenseSaveButton)

        etExpenseAmount.setText(intent.getStringExtra("expenseAmount").toString())
        etExpenseTitle.setText(intent.getStringExtra("expenseTitle").toString())
        etExpenseCategory.setText(intent.getStringExtra("expenseCategory").toString())
        etExpenseDate.setText(intent.getStringExtra("expenseDate").toString())
        etExpenseDescription.setText(intent.getStringExtra("expenseDescription").toString())
        eDialog.setTitle("Updating $expenseTitle Record")

        val alertDialog = eDialog.create()
        alertDialog.show()

        btnExpenseUpdate.setOnClickListener {
            updateExpenseData(
                expenseId,
                etExpenseAmount.text.toString(),
                etExpenseTitle.text.toString(),
                etExpenseCategory.text.toString(),
                etExpenseDate.text.toString(),
                etExpenseDescription.text.toString()
            )
            Toast.makeText(applicationContext, "Expense data updated", Toast.LENGTH_LONG).show()

            //setting updated data to our text views
            tvExpenseAmount.text = etExpenseAmount.text.toString()
            tvExpenseTitle.text = etExpenseTitle.text.toString()
            tvExpenseCategory.text = etExpenseCategory.text.toString()
            tvExpenseDate.text = etExpenseDate.text.toString()
            tvExpenseNote.text = etExpenseDescription.text.toString()
            alertDialog.dismiss()
        }
    }
    private fun updateExpenseData(
        id:String,
        amount: String,
        title:String,
        category:String,
        date:String,
        description:String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Expenses").child(id)
        val expenseInfo = ExpenseModel(id,amount,title,category,date,description)
        dbRef.setValue(expenseInfo)
    }

}