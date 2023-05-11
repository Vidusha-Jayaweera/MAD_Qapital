package com.example.qapital.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.qapital.R
import com.example.qapital.models.IncomeModel
import com.google.firebase.database.FirebaseDatabase

class IncomeDetailsActivity : AppCompatActivity() {
    private lateinit var tvIncomeDate:TextView
    private lateinit var tvIncomeAmount:TextView
    private lateinit var tvIncomeTitle:TextView
    private lateinit var tvIncomeType:TextView
    private lateinit var tvIncomeNote:TextView
    private lateinit var btnUpdate:ImageButton
    private lateinit var btnDelete:ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_income_details)
        tvIncomeDate = findViewById(R.id.tvIncomeDateDetails)
        tvIncomeAmount= findViewById(R.id.tvIncomeAmountDetails)
        tvIncomeTitle= findViewById(R.id.tvIncomeTitleDetails)
        tvIncomeType = findViewById(R.id.tvIncomeCategoryDetails)
        tvIncomeNote = findViewById(R.id.tvIncomeNoteDetails)
        btnUpdate = findViewById(R.id.updateIncomeData)
        btnDelete = findViewById(R.id.deleteIncomeData)
        initView()
        setValuesToViews()
        btnUpdate.setOnClickListener{
            openUpdateDialog(
                intent.getStringExtra("incomeId").toString(),
                intent.getStringExtra("incomeTitle").toString()
            )
        }
        btnDelete.setOnClickListener{
            deleteRecord(
                intent.getStringExtra("incomeId").toString()
            )
        }
    }
    private fun deleteRecord(
        id:String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Incomes").child(id)
        val iTask = dbRef.removeValue()

        iTask.addOnSuccessListener {
            Toast.makeText(this,"IncomeExpense data deleted",Toast.LENGTH_LONG).show()

            val intent = Intent(this,IncomeFetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{error ->
            Toast.makeText(this,"Deleting Err ${error.message}",Toast.LENGTH_LONG).show()
        }
    }
    private fun initView(){}
    private fun setValuesToViews(){
        tvIncomeDate.text = intent.getStringExtra("incomeDate")
        tvIncomeAmount.text = intent.getStringExtra("incomeAmount")
        tvIncomeType.text = intent.getStringExtra("incomeType")
        tvIncomeTitle.text = intent.getStringExtra("incomeTitle")
        tvIncomeNote.text = intent.getStringExtra("incomeNote")
    }
    private fun openUpdateDialog(
        incomeId:String,
        incomeTitle:String
    ){
        val iDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val iDialogView = inflater.inflate(R.layout.activity_income_update,null)

        iDialog.setView(iDialogView)
        val etIncomeAmount = iDialogView.findViewById<EditText>(R.id.etIncomeAmount)
        val etIncomeTitle = iDialogView.findViewById<EditText>(R.id.etIncomeTitle)
        val etIncomeType = iDialogView.findViewById<EditText>(R.id.etIncomeType)
        val etIncomeDate = iDialogView.findViewById<EditText>(R.id.etIncomeDate)
        val etIncomeNote = iDialogView.findViewById<EditText>(R.id.etIncomeNote)
        val btnIncomeUpdate = iDialogView.findViewById<Button>(R.id.IncomeSaveButton)

        etIncomeAmount.setText(intent.getStringExtra("incomeAmount").toString())
        etIncomeTitle.setText(intent.getStringExtra("incomeTitle").toString())
        etIncomeType.setText(intent.getStringExtra("incomeType").toString())
        etIncomeDate.setText(intent.getStringExtra("incomeDate").toString())
        etIncomeNote.setText(intent.getStringExtra("incomeNote").toString())
        iDialog.setTitle("Updating $incomeTitle Record")

        val alertDialog = iDialog.create()
        alertDialog.show()

        btnIncomeUpdate.setOnClickListener {
            updateIncomeData(
                incomeId,
                etIncomeAmount.text.toString(),
                etIncomeTitle.text.toString(),
                etIncomeType.text.toString(),
                etIncomeDate.text.toString(),
                etIncomeNote.text.toString()
            )
            Toast.makeText(applicationContext, "Income data updated", Toast.LENGTH_LONG).show()

            //setting updated data to our text views
            tvIncomeAmount.text = etIncomeAmount.text.toString()
            tvIncomeTitle.text = etIncomeTitle.text.toString()
            tvIncomeType.text = etIncomeType.text.toString()
            tvIncomeDate.text = etIncomeDate.text.toString()
            tvIncomeNote.text = etIncomeNote.text.toString()
            alertDialog.dismiss()
        }
    }
    private fun updateIncomeData(
        id:String,
        amount: String,
        title:String,
        category:String,
        date:String,
        description:String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Incomes").child(id)
        val expenseInfo = IncomeModel(id,amount,title,category,date,description)
        dbRef.setValue(expenseInfo)
    }

}