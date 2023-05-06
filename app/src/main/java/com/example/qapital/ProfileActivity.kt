package com.example.qapital

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ProfileActivity : AppCompatActivity() {

    private lateinit var user_name : TextView
    private lateinit var user_email: TextView
    private lateinit var setting_btn: Button
    private lateinit var backtoHome: ImageButton
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var incomes_btn: Button
    private lateinit var expenses_btn: Button
    private lateinit var debits_btn: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize Firebase instances
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Initialize views
        user_name = findViewById(R.id.user_name)
        user_email = findViewById(R.id.user_email)

        //buttons
        setting_btn = findViewById(R.id.Edit)
        backtoHome = findViewById(R.id.backtoHome)
        incomes_btn = findViewById(R.id.incomes_btn)
        expenses_btn = findViewById(R.id.expenses_btn)
        debits_btn = findViewById(R.id.debits_btn)


        // Get the current user's ID
        val userId = auth.currentUser?.uid

        // Retrieve the user's name from Firebase
        if (userId != null) {
            db.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    val userName = document.getString("name")
                    user_name.text = userName

                    val userEmail = document.getString("email")
                    user_email.text = userEmail
                }

                .addOnFailureListener { exception ->
                    // Handle errors
                }
        }

        setting_btn.setOnClickListener {
            val intent = Intent(this@ProfileActivity, EditActivity::class.java)
            startActivity(intent)
        }

        backtoHome.setOnClickListener(){
            val intent = Intent(this@ProfileActivity, HomeActivity::class.java)
            startActivity(intent)
        }
    }

}
