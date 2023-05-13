package com.example.qapital.activities

import android.content.Intent
import android.icu.util.CurrencyAmount
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.qapital.R
import com.example.qapital.models.ExpenseModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class ExpenseDetailsActivity : AppCompatActivity() {
    private lateinit var tvExpenseDate:TextView
    private lateinit var tvExpenseAmount:TextView
    private lateinit var tvExpenseTitle:TextView
    private lateinit var tvExpenseCategory:TextView
    private lateinit var tvExpenseNote:TextView
    private lateinit var btnUpdate:ImageButton
    private lateinit var btnDelete:ImageButton
    private lateinit var expenseId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_details)
        // Get the expenseID from the intent extras
        expenseId = intent.getStringExtra("expenseId").toString()
        //Back button implementation
        val backButton: ImageButton = findViewById(R.id.backBtn)
        backButton.setOnClickListener {
            finish()
        }
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
    private fun deleteRecord(id: String) {
        //Initialize Firebase Auth and firebase database
        val user = Firebase.auth.currentUser
        val uid = user?.uid
        if (uid != null) {
            // Create an AlertDialog to confirm the deletion
            val alertDialog = AlertDialog.Builder(this)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this expense?")
                .setPositiveButton("Delete") { _, _ ->
                    // If the user confirms the deletion, remove the expense from the database
                    val dbRef = FirebaseDatabase.getInstance().getReference(uid).child(expenseId) //initialize database with uid as the parent
                    val eTask = dbRef.removeValue()
                        eTask.addOnSuccessListener {
                            Toast.makeText(this, "Expense data deleted", Toast.LENGTH_LONG).show()

                            // Start the ExpenseFetchingActivity to refresh the expense list
                            val intent = Intent(this, ExpenseFetchingActivity::class.java)
                            finish()
                            startActivity(intent)
                        }
                        .addOnFailureListener { error ->
                            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG)
                                .show()
                        }
                }

                .setNegativeButton("Cancel") { _, _ ->
                    // If the user cancels the deletion, do nothing
                }
                .create()

            alertDialog.show()
        }
    }

    private fun initView(){}
    private fun setValuesToViews(){
        tvExpenseDate.text = intent.getStringExtra("expenseDate")
        val expenseAmount: Double = intent.getDoubleExtra("expenseAmount", 0.0)
        tvExpenseAmount.text = expenseAmount.toString()
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
        val etExpenseCategory = eDialogView.findViewById<AutoCompleteTextView>(R.id.etExpenseCategory)
        val etExpenseDate = eDialogView.findViewById<EditText>(R.id.etExpenseDate)
        val etExpenseDescription = eDialogView.findViewById<EditText>(R.id.etExpenseDescription)
        val btnExpenseUpdate = eDialogView.findViewById<Button>(R.id.expenseSaveButton)


        etExpenseAmount.setText(intent.getDoubleExtra("expenseAmount", 0.0).toString())
        etExpenseTitle.setText(intent.getStringExtra("expenseTitle").toString())
        //set text to auto complete text view category:
        val categoryOld = (intent.getStringExtra("expenseCategory"))
        etExpenseCategory.setText(categoryOld)
        val expenseId = intent.getStringExtra("expenseId") //store debt id
        val listExpense = CategoryOptions.expenseCategory() //getting the arrayList data from CategoryOptions file
        val expenseAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listExpense)
        etExpenseCategory.setAdapter(expenseAdapter)
        etExpenseDate.setText(intent.getStringExtra("expenseDate").toString())
        etExpenseDescription.setText(intent.getStringExtra("expenseDescription").toString())
        eDialog.setTitle("Updating $expenseTitle Record")

        val alertDialog = eDialog.create()
        alertDialog.show()

        btnExpenseUpdate.setOnClickListener {
            updateExpenseData(
                expenseId.toString(),
                etExpenseAmount.text.toString().toDouble(),
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
        amount: Double,
        title:String,
        category:String,
        date:String,
        description:String
    ){
        //Initialize Firebase Auth and firebase database
        val user = Firebase.auth.currentUser
        val uid = user?.uid
        if (uid != null) {
            val dbRef = FirebaseDatabase.getInstance().getReference(uid) //initialize database with uid as the parent
            val expenseInfo = ExpenseModel(id,amount,title,category,date,description)
            dbRef.child(expenseId).setValue(expenseInfo)
        }
    }

}