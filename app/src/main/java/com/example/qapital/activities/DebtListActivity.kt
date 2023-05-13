package com.example.qapital.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.qapital.R
import com.example.qapital.adapters.DebtAdapter
import com.example.qapital.models.DebtModel
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class DebtListActivity : AppCompatActivity() {

    private lateinit var debtRecyclerView: RecyclerView
    private lateinit var debtList: ArrayList<DebtModel>
    private lateinit var shimmerLoading: ShimmerFrameLayout
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var tvVisibilityNoData: TextView
    private lateinit var tvNoData: TextView
    private lateinit var noDataImage: ImageView
    private lateinit var tvNoDataTitle: TextView
    private lateinit var dbRef: DatabaseReference
    private val user = Firebase.auth.currentUser
    private var selectedType: String = "All Type"
    private lateinit var typeOption: Spinner
    private lateinit var exportButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debt_list)

        debtRecyclerView = findViewById(R.id.rvDebts)
        debtRecyclerView.layoutManager = LinearLayoutManager(this)
        debtRecyclerView.setHasFixedSize(true)
        debtList = arrayListOf<DebtModel>()

        tvNoData = findViewById(R.id.tvNoData)
        noDataImage = findViewById(R.id.noDataImage)
        tvNoDataTitle = findViewById(R.id.tvNoDataTitle)
        tvVisibilityNoData = findViewById(R.id.visibilityNoData)
        shimmerLoading = findViewById(R.id.shimmerFrameLayout)
        exportButton = findViewById(R.id.exportButton)

        getDebtData()
        visibilityOptions()

        swipeRefreshLayout = findViewById(R.id.swipeRefresh)
        swipeRefreshLayout.setOnRefreshListener { //call getTransaction() back to refresh the recyclerview
            getDebtData()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun visibilityOptions(){
        typeOption = findViewById<Spinner>(R.id.typeSpinner)
        val typeList = arrayOf("All Type", "Paid", "Not paid")
        val typeSpinnerAdapter = ArrayAdapter<String>(this,R.layout.selected_spinner,typeList)
        typeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
        typeOption.adapter = typeSpinnerAdapter

        typeOption.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when(typeList[p2]){
                    "All Type" -> selectedType = "All Type"
                    "Paid" -> selectedType = "Paid"
                    "Not paid" -> selectedType = "Not paid"
                }
                getDebtData()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    private fun getDebtData() {
        shimmerLoading.startShimmerAnimation()
        shimmerLoading.visibility = View.VISIBLE
        tvVisibilityNoData.visibility = View.GONE
        debtRecyclerView.visibility = View.GONE //hide the recycler view
        tvNoData.visibility = View.GONE
        noDataImage.visibility = View.GONE
        tvNoDataTitle.visibility = View.GONE

        val uid = user?.uid //get user id from database
        if (uid != null) {
            dbRef = FirebaseDatabase.getInstance().getReference(uid)
        }

        val query: Query = dbRef.orderByChild("invertedBorrowedDate") //sorting date descending
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                debtList.clear()
                if (snapshot.exists()){
                    when (selectedType) {
                        "All Type" -> { //all option selected
                            for (debtSnap in snapshot.children){
                                val debtData = debtSnap.getValue(DebtModel::class.java) //reference data class
                                debtList.add(debtData!!)
                            }
                        }
                        "Paid" -> { //paid option selected
                            for (debtSnap in snapshot.children){
                                val debtData = debtSnap.getValue(DebtModel::class.java) //reference data class
                                if (debtData!!.payStatus == "Paid")
                                    debtList.add(debtData!!)
                            }
                        }
                        "Not paid" -> { //paid option selected
                            for (debtSnap in snapshot.children){
                                val debtData = debtSnap.getValue(DebtModel::class.java) //reference data class
                                if (debtData!!.payStatus == "Not paid")
                                    debtList.add(debtData!!)
                            }
                        }
                    }

                    if (debtList.isEmpty()){ //if there is no data being displayed
                        noDataImage.visibility = View.VISIBLE
                        tvNoDataTitle.visibility = View.VISIBLE
                        tvVisibilityNoData.visibility = View.VISIBLE
                        tvVisibilityNoData.text = "There is no $selectedType data"
                    }else{
                        val mAdapter = DebtAdapter(debtList)
                        debtRecyclerView.adapter = mAdapter

                        mAdapter.setOnItemClickListener(object: DebtAdapter.onItemClickListener{ //item click listener and pass extra data
                            override fun onItemClick(position: Int) {
                                val intent = Intent(this@DebtListActivity, DebtDetailsActivity::class.java)

                                //put extras
                                intent.putExtra("debtId", debtList[position].debtId)
                                intent.putExtra("debtName", debtList[position].debtName)
                                intent.putExtra("debtAmount", debtList[position].debtAmount)
                                intent.putExtra("debtBorrowedDate", debtList[position].debtBorrowedDate)
                                intent.putExtra("debtReturnDate", debtList[position].debtReturnDate)
                                intent.putExtra("debtNote", debtList[position].debtNote)
                                intent.putExtra("payStatus", debtList[position].payStatus)
                                startActivity(intent)
                            }
                        })
                        debtRecyclerView.visibility = View.VISIBLE
                    }
                    shimmerLoading.stopShimmerAnimation()
                    shimmerLoading.visibility = View.GONE
                }else{ //if there is no data in database
                    shimmerLoading.stopShimmerAnimation()
                    shimmerLoading.visibility = View.GONE

                    noDataImage.visibility = View.VISIBLE
                    tvNoDataTitle.visibility = View.VISIBLE
                    tvNoData.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                print("Listener was cancelled")
            }

        })
    }
}