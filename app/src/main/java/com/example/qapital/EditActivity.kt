package com.example.qapital

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button


    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        nameEditText = findViewById(R.id.ChangeName)
        passwordEditText = findViewById(R.id.ChangePassword)
        saveButton = findViewById(R.id.save_btn)
        deleteButton = findViewById(R.id.delete_btn)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        userId = auth.currentUser?.uid.toString()

        saveButton.setOnClickListener {
            val newName = nameEditText.text.toString().trim()
            val newPassword = passwordEditText.text.toString().trim()

            if (newName.isNotEmpty()) {
                updateName(newName)
            }

            if (newPassword.isNotEmpty()) {
                updatePassword(newPassword)
            }

            Toast.makeText(this, "Changes saved successfully", Toast.LENGTH_SHORT).show()
            //redirect to main activity
            val intent = Intent(this@EditActivity, ProfileActivity::class.java)
            startActivity(intent)
        }

        deleteButton.setOnClickListener {
            deleteAccount()
        }
    }

    private fun updateName(newName: String) {
        firestore.collection("users").document(userId)
            .update("name", newName)
            .addOnSuccessListener {
                nameEditText.setText(newName)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to update name: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updatePassword(newPassword: String) {
        auth.currentUser?.updatePassword(newPassword)
            ?.addOnSuccessListener {
                passwordEditText.setText("")
            }
            ?.addOnFailureListener { e ->
                Toast.makeText(this, "Failed to update password: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteAccount() {
        firestore.collection("users").document(userId)
            auth.currentUser?.delete()
                    ?.addOnSuccessListener {
                        Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@EditActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    }
                    ?.addOnFailureListener { e ->
                        Toast.makeText(this, "Failed to delete account: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
    }
}
