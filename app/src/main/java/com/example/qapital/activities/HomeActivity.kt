package com.example.qapital.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.qapital.R
import com.example.qapital.models.DebtModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import java.util.Calendar

class HomeActivity : AppCompatActivity() {

    private lateinit var GoUserProfileBtn: ImageButton
    private lateinit var GreatingText: TextView
    private lateinit var UserName: TextView
    private lateinit var floatingActionBtn : FloatingActionButton

    //firestrore connections
    private var auth: FirebaseAuth = Firebase.auth
    private lateinit var db: FirebaseFirestore


    private var user = Firebase.auth.currentUser
    private val uid = user?.uid //get user id from database
    private var dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference(uid!!)

    //initialize var for storing amount value from db
    var allTimeExpense: Double = 0.0
    var allTimeIncome: Double = 0.0
    var allTimeDebt: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize Firebase instances
//        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Initialize button
        GoUserProfileBtn = findViewById(R.id.GoUserProfileBtn)
        floatingActionBtn = findViewById(R.id.floatingActionButton)

        // Initialize TextView
        GreatingText = findViewById(R.id.GreatingText)
        UserName = findViewById(R.id.NameUser)

        fetchAmount()

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

        floatingActionBtn.setOnClickListener{
            val intent = Intent(this@HomeActivity, DebtInsertionActivity::class.java)
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

    private fun showAllTimeRecap() {

        val tvNetAmount: TextView = findViewById(R.id.netAmount)
        val tvAmountExpense: TextView = findViewById(R.id.expenseAmount)
        val tvAmountIncome: TextView = findViewById(R.id.incomeAmount)
        val tvAmountDebt: TextView = findViewById(R.id.debtAmount)

//        tvNetAmount.text = "${allTimeIncome-allTimeExpense}"
//        tvAmountExpense.text = "$allTimeExpense"
//        tvAmountIncome.text = "$allTimeIncome"
          tvAmountDebt.text = "$allTimeDebt"
    }

    private fun fetchAmount() {
        var amountExpenseTemp = 0.0
        var amountIncomeTemp = 0.0
        var amountDebtTemp = 0.0

        val debtList: ArrayList<DebtModel> = arrayListOf<DebtModel>()

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                debtList.clear()
                if (snapshot.exists()) {
                    for (debtSnap in snapshot.children) {
                        val debtData = debtSnap.getValue(DebtModel::class.java)
                        debtList.add(debtData!!)
                    }
                }

                //take all amount expense, income and debts:
                for ((i) in debtList.withIndex()){
                    amountDebtTemp += debtList[i].debtAmount!!
                }
                allTimeDebt = amountDebtTemp

                showAllTimeRecap()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

}

