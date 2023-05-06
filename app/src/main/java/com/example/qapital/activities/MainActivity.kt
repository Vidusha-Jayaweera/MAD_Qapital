package com.example.qapital.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.qapital.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.my_button)
        button.setOnClickListener {
            val intent = Intent(this, DebtInsertionActivity::class.java)
            startActivity(intent)
        }
    }
}