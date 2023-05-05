package com.example.qapital.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.qapital.R
import java.text.SimpleDateFormat
import java.util.*

class DebtDetailsActivity : AppCompatActivity() {

    private lateinit var tvAmountDetails: TextView
    private lateinit var tvPayStatusDetails: TextView
    private lateinit var tvNameDetails: TextView
    private lateinit var tvBorrowedDateDetails: TextView
    private lateinit var tvReturnDateDetails: TextView
    private lateinit var tvNoteDetails: TextView
    private lateinit var paidButton: Button
    private lateinit var paidIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debt_details)

        initView()
        setValuesToViews()
    }

    private fun initView() { //initialized ui item id
        tvAmountDetails = findViewById(R.id.tvAmountDetails)
        tvPayStatusDetails = findViewById(R.id.tvPayStatusDetails)
        tvNameDetails = findViewById(R.id.tvNameDetails)
        tvBorrowedDateDetails = findViewById(R.id.tvBorrowedDateDetails)
        tvNoteDetails = findViewById(R.id.tvNoteDetails)
        tvReturnDateDetails = findViewById(R.id.tvReturnDateDetails)
        tvNoteDetails = findViewById(R.id.tvNoteDetails)
        paidButton = findViewById(R.id.paidButton)
        paidIcon = findViewById(R.id.paidIcon)
    }

    private fun setValuesToViews(){

        tvNameDetails.text =  intent.getStringExtra("debtName")
        val debtPayStatus: String? = intent.getStringExtra("payStatus")
        val debtAmount: Double = intent.getDoubleExtra("debtAmount", 0.0)
        tvAmountDetails.text = debtAmount.toString()

        if (debtPayStatus == "Not paid") {
            tvPayStatusDetails.text = "Not paid"
            paidButton.visibility = View.VISIBLE
            paidIcon.visibility = View.GONE
        }else{
            tvPayStatusDetails.text = "Paid"
            paidIcon.visibility = View.VISIBLE
            paidButton.visibility = View.GONE
        }

        val debtBorrowedDate: Long = intent.getLongExtra("debtBorrowedDate", 0)
        val debtReturnDate: Long = intent.getLongExtra("debtReturnDate", 0)
        val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
        val resultBorrowedDate = Date(debtBorrowedDate)
        val resultReturnDate = Date(debtReturnDate)
        tvBorrowedDateDetails.text = simpleDateFormat.format(resultBorrowedDate)
        tvReturnDateDetails.text = simpleDateFormat.format(resultReturnDate)

        tvNoteDetails.text =  intent.getStringExtra("debtNote")

    }
}