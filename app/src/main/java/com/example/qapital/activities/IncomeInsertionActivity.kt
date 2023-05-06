package com.example.qapital.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.qapital.models.IncomeModel
import com.example.qapital.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_income_insertion.*

class IncomeInsertionActivity : AppCompatActivity() {

    private lateinit var etIncomeAmount:EditText
    private lateinit var etIncomeTitle:EditText
    private lateinit var etIncomeType:EditText
    private lateinit var etIncomeDate:EditText
    private lateinit var etIncomeNote:EditText
    private lateinit var btnIncomeSave:Button

    private lateinit var dbRef:DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_income_insertion)

        etIncomeAmount = findViewById(R.id.etIncomeAmount)
        etIncomeTitle = findViewById(R.id.etIncomeTitle)
        etIncomeType = findViewById(R.id.etIncomeType)
        etIncomeDate = findViewById(R.id.etIncomeDate)
        etIncomeNote = findViewById(R.id.etIncomeNote)
        btnIncomeSave = findViewById(R.id.IncomeSaveButton)

        dbRef = FirebaseDatabase.getInstance().getReference("Incomes")

        btnIncomeSave.setOnClickListener{
            saveIncomeData()
        }

    }
    private fun saveIncomeData(){
        //getting values
        val incomeAmount = etIncomeAmount.text.toString()
        val incomeTitle = etIncomeTitle.text.toString()
        val incomeType = etIncomeType.text.toString()
        val incomeDate = etIncomeDate.text.toString()
        val incomeNote = etIncomeNote.text.toString()

        if(incomeAmount.isEmpty()){
            etIncomeAmount.error = "Please enter amount"
        }
        if(incomeTitle.isEmpty()){
            etIncomeTitle.error = "Please enter title"
        }
        if(incomeType.isEmpty()){
            etIncomeType.error = "Please enter category"
        }
        if(incomeDate.isEmpty()){
            etIncomeDate.error = "Please enter date"
        }
        if(incomeNote.isEmpty()){
            etIncomeNote.error = "Please enter description"
        }
        val incomeId = dbRef.push().key!!

        val income = IncomeModel(incomeId,incomeAmount,incomeTitle,incomeType,incomeDate,incomeNote)

        dbRef.child(incomeId).setValue(income)
            .addOnCompleteListener{
                Toast.makeText(this,"Data inserted successfuly",Toast.LENGTH_LONG).show()

                etIncomeAmount.text.clear()
                etIncomeTitle.text.clear()
                etIncomeType.text.clear()
                etIncomeDate.text.clear()
                etIncomeNote.text.clear()


            }.addOnFailureListener{err->
                Toast.makeText(this,"Error ${err.message}",Toast.LENGTH_LONG).show()
            }
    }
}
