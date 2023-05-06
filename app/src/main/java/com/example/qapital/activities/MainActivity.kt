package com.example.qapital.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.qapital.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var incomeButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       val firebase : DatabaseReference = FirebaseDatabase.getInstance().getReference()

        val button = findViewById<Button>(R.id.my_button)
        button.setOnClickListener {
            val intent = Intent(this, IncomeInsertionActivity::class.java)
            startActivity(intent)
        }
        incomeButton = findViewById<Button>(R.id.incomeButton)

        incomeButton.setOnClickListener{
            val intent = Intent(this, IncomeFetchingActivity::class.java)
            startActivity(intent)
        }

    }


}