package com.example.qapital.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qapital.R
import com.example.qapital.adapters.ExpenseAdapter
import com.example.qapital.models.ExpenseModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class ExpenseFetchingActivity : AppCompatActivity() {

    private lateinit var expenseRecyclerView: RecyclerView
    private lateinit var expenseList:ArrayList<ExpenseModel>
    private lateinit var dbRef: DatabaseReference
    private val user = Firebase.auth.currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_fetching)

        expenseRecyclerView = findViewById(R.id.rvExpenses)
        expenseRecyclerView.layoutManager = LinearLayoutManager(this)
        expenseRecyclerView.setHasFixedSize(true)

        expenseList = arrayListOf<ExpenseModel>()

        getExpensesData()
    }

    private fun getExpensesData(){
        expenseRecyclerView.visibility = View.GONE
        val uid = user?.uid //get user id from database
        if (uid != null) {
            dbRef = FirebaseDatabase.getInstance().getReference(uid)
        }

        dbRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                expenseList.clear()
                if(snapshot.exists()){
                    for(expenseSnap in snapshot.children){
                        val expenseData = expenseSnap.getValue(ExpenseModel::class.java)
                        expenseList.add(expenseData!!)
                    }
                    val eAdapter = ExpenseAdapter(expenseList)
                    expenseRecyclerView.adapter=eAdapter

                    eAdapter.setOnExpenseItemClickListener(object : ExpenseAdapter.onExpenseItemClickListener{
                        override fun onExpenseItemClick(position: Int) {
                            val intent = Intent(this@ExpenseFetchingActivity,ExpenseDetailsActivity::class.java)

                            //put extras
                            intent.putExtra("expenseId",expenseList[position].expenseId)
                            intent.putExtra("expenseAmount",expenseList[position].expenseAmount)
                            intent.putExtra("expenseTitle",expenseList[position].expenseTitle)
                            intent.putExtra("expenseCategory",expenseList[position].expenseCategory)
                            intent.putExtra("expenseDate",expenseList[position].expenseDate)
                            intent.putExtra("expenseDescription",expenseList[position].expenseDescription)
                            startActivity(intent)
                        }

                    })



                    expenseRecyclerView.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}