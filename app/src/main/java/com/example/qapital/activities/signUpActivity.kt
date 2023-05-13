package com.example.qapital.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.view.MenuItem
import com.example.qapital.R

class signUpActivity : AppCompatActivity() {

    //declare variables
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    //onCreate method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //initialize variables
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        //set onClickListener for signUpButton
        val signUpButton = findViewById<Button>(R.id.signupbutton)
        signUpButton.setOnClickListener {
            signUp()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true) // enable the up button
    }

    //onOptionsItemSelected method
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { // handle up button click
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //signUp method
    private fun signUp() {
        //declare variables
        val nameEditText = findViewById<EditText>(R.id.signUpName)
        val emailEditText = findViewById<EditText>(R.id.signupemail)
        val passwordEditText = findViewById<EditText>(R.id.signupPassword)

        //get name, email, and password from editTexts
        val name = nameEditText.text.toString()
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        //check if name, email, and password are empty
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            return
        }

        //create user with email and password
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userId = user?.uid

                    val userMap = hashMapOf(
                        "name" to name,
                        "email" to email
                    )

                    //add user to firestore
                    firestore.collection("users").document(userId!!)
                        .set(userMap)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Signup Successful!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, SignInActivity::class.java)
                            startActivity(intent)
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }
}