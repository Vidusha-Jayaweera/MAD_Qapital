package com.example.qapital

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.TextView
import java.util.Calendar

class HomeActivity : AppCompatActivity() {

    private lateinit var GoUserProfileBtn: ImageButton
    private lateinit var GreatingText: TextView
    private lateinit var UserName: TextView

    //firestrore connections
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize Firebase instances
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Initialize button
        GoUserProfileBtn = findViewById(R.id.GoUserProfileBtn)

        // Initialize TextView
        GreatingText = findViewById(R.id.GreatingText)
        UserName = findViewById(R.id.NameUser)

        // Get the current user's ID
        val userId = auth.currentUser?.uid

        // Retrieve the user's name from Firebase
        if (userId != null) {
            db.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    val userName = document.getString("name")
                    UserName.text = userName
                }
                .addOnFailureListener { exception ->
                    // Handle errors
                }
        }

        // Set onClickListener for GoUserProfileBtn
        GoUserProfileBtn.setOnClickListener {
            // Redirect to ProfileActivity
            val intent = Intent(this@HomeActivity, ProfileActivity::class.java)
            startActivity(intent)
        }

        // Get the current hour of the day
        val hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        // Set the greeting text based on the time of day
        GreatingText.text = when(hourOfDay) {
            in 0..11 -> "Good Morning"
            in 12..15 -> "Good Afternoon"
            else -> "Good Evening"
        }
    }
}

