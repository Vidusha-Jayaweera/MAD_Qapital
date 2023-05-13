package com.example.qapital.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qapital.R
import com.example.qapital.adapters.IncomeAdapter
import com.example.qapital.models.IncomeModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class IncomeFetchingActivity : AppCompatActivity() {

    private lateinit var incomeRecyclerView: RecyclerView
    private lateinit var tvIncomeLoadingData: TextView
    private lateinit var incomeList:ArrayList<IncomeModel>
    private lateinit var dbRef: DatabaseReference
    private val user = Firebase.auth.currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_income_fetching)

        incomeRecyclerView = findViewById(R.id.rvIncome)
        incomeRecyclerView.layoutManager = LinearLayoutManager(this)
        incomeRecyclerView.setHasFixedSize(true)
        tvIncomeLoadingData = findViewById(R.id.tvLoadingIncomeData)

        incomeList = arrayListOf<IncomeModel>()

        getIncomesData()
    }

    private fun getIncomesData(){
        incomeRecyclerView.visibility = View.GONE
        tvIncomeLoadingData.visibility = View.VISIBLE
        val uid = user?.uid //get user id from database
        if (uid != null) {
            dbRef = FirebaseDatabase.getInstance().getReference(uid)
        }
        dbRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                incomeList.clear()
                if(snapshot.exists()){
                    for(incomeSnap in snapshot.children){
                        val incomeData = incomeSnap.getValue(IncomeModel::class.java)
                        incomeList.add(incomeData!!)
                    }
                    val iAdapter = IncomeAdapter(incomeList)
                    incomeRecyclerView.adapter=iAdapter

                    iAdapter.setOnIncomeItemClickListener(object : IncomeAdapter.onIncomeItemClickListener{
                        override fun onIncomeItemClick(position: Int) {
                            val intent = Intent(this@IncomeFetchingActivity,IncomeDetailsActivity::class.java)

                            //put extras
                            intent.putExtra("incomeId",incomeList[position].incomeId)
                            intent.putExtra("incomeAmount",incomeList[position].incomeAmount)
                            intent.putExtra("incomeTitle",incomeList[position].incomeTitle)
                            intent.putExtra("incomeType",incomeList[position].incomeType)
                            intent.putExtra("incomeDate",incomeList[position].incomeDate)
                            intent.putExtra("incomeNote",incomeList[position].incomeNote)
                            startActivity(intent)
                        }

                    })



                    incomeRecyclerView.visibility = View.VISIBLE
                    tvIncomeLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}