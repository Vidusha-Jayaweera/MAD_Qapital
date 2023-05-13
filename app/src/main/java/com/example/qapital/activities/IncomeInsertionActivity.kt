package com.example.qapital.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.qapital.models.IncomeModel
import com.example.qapital.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class IncomeInsertionActivity : AppCompatActivity() {

    private lateinit var etIncomeAmount:EditText
    private lateinit var etIncomeTitle:EditText
    private lateinit var etIncomeType:EditText
    private lateinit var etIncomeDate:EditText
    private lateinit var etIncomeNote:EditText
    private lateinit var btnIncomeSave:Button
    private var incomeAmount: Double = 0.0
    private lateinit var dbRef:DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_income_insertion)
        //Back button implementation
        val backButton: ImageButton = findViewById(R.id.backBtn)
        backButton.setOnClickListener {
            finish()
        }

        etIncomeAmount = findViewById(R.id.etIncomeAmount)
        etIncomeTitle = findViewById(R.id.etIncomeTitle)
        etIncomeType = findViewById(R.id.etIncomeType)
        etIncomeDate = findViewById(R.id.etIncomeDate)
        etIncomeNote = findViewById(R.id.etIncomeNote)
        btnIncomeSave = findViewById(R.id.IncomeSaveButton)

        //Initialize Firebase Auth and firebase database--
        val user = Firebase.auth.currentUser
        val uid = user?.uid
        if (uid != null) {
            dbRef = FirebaseDatabase.getInstance().getReference(uid) //initialize database with uid as the parent
        }
        auth = Firebase.auth

        btnIncomeSave.setOnClickListener{
            saveIncomeData()
        }

    }
    private fun saveIncomeData(){
        //getting values
        val incomeAmountEt = etIncomeAmount.text.toString()
        val incomeTitle = etIncomeTitle.text.toString()
        val incomeType = etIncomeType.text.toString()
        val incomeDate = etIncomeDate.text.toString()
        val incomeNote = etIncomeNote.text.toString()

        if(incomeAmountEt.isEmpty()){
            etIncomeAmount.error = "Please enter amount"
        }
        else if(incomeTitle.isEmpty()){
            etIncomeTitle.error = "Please enter title"
        }
        else if(incomeType.isEmpty()){
            etIncomeType.error = "Please enter category"
        }
        else if(incomeDate.isEmpty()){
            etIncomeDate.error = "Please enter date"
        }
        else if(incomeNote.isEmpty()){
            etIncomeNote.error = "Please enter description"
        }
        else{
            incomeAmount = etIncomeAmount.text.toString().toDouble() //convert to double type
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
