package com.example.qapital

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class SignInActivity : AppCompatActivity() {

    //declare variables
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signInButton: Button
    private lateinit var firebaseAuth: FirebaseAuth

    //onCreate method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        //initialize variables
        emailEditText = findViewById(R.id.LogingTextEmailAddress)
        passwordEditText = findViewById(R.id.LogingTextPassword)
        signInButton = findViewById(R.id.btn_sign_in)

        //initialize firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        //set onClickListener for signInButton
        signInButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            //check if email and password are empty
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter your email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //sign in with email and password
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = firebaseAuth.currentUser
                        Toast.makeText(this, "Welcome ${user?.email}", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@SignInActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        if (task.exception is FirebaseAuthInvalidUserException) {
                            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()
                        } else if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }
    }

    //companion object
    companion object {
        private const val TAG = "SignInActivity"
    }
}
