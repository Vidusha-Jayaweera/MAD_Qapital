package com.example.qapital.activities

import android.app.DatePickerDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.qapital.R
import com.example.qapital.models.DebtModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class DebtDetailsActivity : AppCompatActivity() {

    lateinit var tvAmountDetails: TextView
    private lateinit var tvPayStatusDetails: TextView
    lateinit var tvNameDetails: TextView
    lateinit var tvBorrowedDateDetails: TextView
    lateinit var tvReturnDateDetails: TextView
    lateinit var tvNoteDetails: TextView
    private lateinit var paidButton: Button
    private lateinit var paidIcon: ImageView
    private lateinit var debtId: String

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debt_details)

        // Get the debt ID from the intent extras
        debtId = intent.getStringExtra("debtId").toString()

        //back button
        val backButton: ImageButton = findViewById(R.id.backBtn)
        backButton.setOnClickListener {
            finish()
        }

        //update data
        val updateDebt: ImageButton = findViewById(R.id.btnUpdateDebt)
        updateDebt.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("debtName").toString()
            )
        }

        deleteData()

        initView()
        setValuesToViews()

        //set pay status as paid
        paidButton.setOnClickListener {
            updatePayStatus("Paid")
        }
    }

    private fun deleteData() {
        val deleteDebt: ImageButton = findViewById(R.id.btnDeleteDebt)
        val alertBox = AlertDialog.Builder(this)
        deleteDebt.setOnClickListener {
            alertBox.setTitle("Are you sure?")
            alertBox.setMessage("Do you want to delete this debt?")
            alertBox.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                deleteRecord(
                    intent.getStringExtra("debtId").toString()
                )
            }
            alertBox.setNegativeButton("No") { _: DialogInterface, _: Int -> }
            alertBox.show()
        }
    }

    private fun deleteRecord(debtId: String) {
        //Initialize Firebase Auth and firebase database
        val user = Firebase.auth.currentUser
        val uid = user?.uid
        if (uid != null) {
            val dbRef = FirebaseDatabase.getInstance().getReference(uid).child(debtId) //initialize database with uid as the parent
            val mTask = dbRef.removeValue()

            mTask.addOnSuccessListener {
                Toast.makeText(this, "Debt Data Deleted", Toast.LENGTH_LONG).show()
                finish()
            }.addOnFailureListener { error ->
                Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
            }
        }
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

    private fun updatePayStatus(payStatus: String) {
        val user = Firebase.auth.currentUser
        val uid = user?.uid
        if (uid != null) {
            val dbRef = FirebaseDatabase.getInstance().getReference(uid).child(debtId)
            dbRef.child("payStatus").setValue(payStatus).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Pay status updated to $payStatus", Toast.LENGTH_LONG)
                        .show()
                    tvPayStatusDetails.text = payStatus
                    paidIcon.visibility = View.VISIBLE
                    paidButton.visibility = View.GONE
                } else {
                    Toast.makeText(this, "Failed to update pay status", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun openUpdateDialog(
        debtName: String
    ){
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.activity_debt_update, null)

        mDialog.setView(mDialogView)

        //Initialize items
        val etDebtAmount = mDialogView.findViewById<EditText>(R.id.etDebtAmountUpdate)
        val etDebtName = mDialogView.findViewById<EditText>(R.id.etDebtNameUpdate)
        val etBorrowedDate = mDialogView.findViewById<EditText>(R.id.etBorrowedDateUpdate)
        val etReturnDate = mDialogView.findViewById<EditText>(R.id.etReturnDateUpdate)
        val etDebtNote = mDialogView.findViewById<EditText>(R.id.etDebtNoteUpdate)
        val btnUpdateData = mDialogView.findViewById<Button>(R.id.updateButton)
        val btnUpdateCancel = mDialogView.findViewById<Button>(R.id.updateCancelButton)

        etDebtAmount.setText(intent.getDoubleExtra("debtAmount", 0.0).toString())
        etDebtName.setText(intent.getStringExtra("debtName").toString())
        etDebtNote.setText(intent.getStringExtra("debtNote").toString())

        val debtId = intent.getStringExtra("debtId") //store debt id

        //---set text to date edit text and date picker:---
        val borrowedDate: Long = intent.getLongExtra("debtBorrowedDate", 0)
        val returnDate: Long = intent.getLongExtra("debtReturnDate", 0)
        val cal = Calendar.getInstance()
        //convert millis to date format
        val getBorrowedDate = Date(borrowedDate)
        val getReturnDate = Date(returnDate)
        cal.time = getBorrowedDate
        cal.time = getReturnDate

        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val resultParseBorrowedDate = simpleDateFormat.format(getBorrowedDate)
        val resultParseReturnDate = simpleDateFormat.format(getReturnDate)
        etBorrowedDate.setText(resultParseBorrowedDate)
        etReturnDate.setText(resultParseReturnDate)

        //initialized current date value on db
        var borrowedDateUpdate: Long = intent.getLongExtra("debtBorrowedDate", 0)
        var returnDateUpdate: Long = intent.getLongExtra("debtReturnDate", 0)
        var invertedBorrowedDate: Long = borrowedDateUpdate * -1
        var invertedReturnDate: Long = returnDateUpdate * -1

        etBorrowedDate.setOnClickListener {
            val year = cal.get(Calendar.YEAR) //set default year in datePickerDialog similar with database data
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this,
                { _, selectedYear, selectedMonth, selectedDayOfMonth ->

                    val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                    etBorrowedDate.text = null
                    etBorrowedDate.hint = selectedDate

                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                    val theDate = sdf.parse(selectedDate)
                    borrowedDateUpdate = theDate!!.time //convert date to millisecond
                    invertedBorrowedDate = borrowedDateUpdate * -1
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        etReturnDate.setOnClickListener {
            val year = cal.get(Calendar.YEAR) //set default year in datePickerDialog similar with database data
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this,
                { _, selectedYear, selectedMonth, selectedDayOfMonth ->

                    val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                    etReturnDate.text = null
                    etReturnDate.hint = selectedDate

                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                    val theDate = sdf.parse(selectedDate)
                    returnDateUpdate = theDate!!.time //convert date to millisecond
                    invertedReturnDate = returnDateUpdate * -1
                },
                year,
                month,
                day
            )
            dpd.show()
        }
        //-------

        mDialog.setTitle("Updating $debtName's Debt")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {

            updateDebtData(
                debtId.toString(),
                etDebtAmount.text.toString().toDouble(),
                etDebtName.text.toString(),
                borrowedDateUpdate,
                returnDateUpdate,
                etDebtNote.text.toString(),
                invertedBorrowedDate,
                invertedReturnDate
            )
            Toast.makeText(applicationContext, "Debt Data Updated", Toast.LENGTH_LONG).show()

            //setting updated data to textviews :
            tvAmountDetails.text = etDebtAmount.text.toString()
            tvNameDetails.text = etDebtName.text.toString()
            tvNoteDetails.text = etDebtNote.text.toString()

            val borrowedDate: Long = borrowedDateUpdate
            val returnDate: Long = returnDateUpdate
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            val borrowedDateResult = Date(borrowedDate)
            val returnDateResult = Date(returnDate)
            tvBorrowedDateDetails.text = simpleDateFormat.format(borrowedDateResult)
            tvReturnDateDetails.text = simpleDateFormat.format(returnDateResult)

            alertDialog.dismiss()
        }

        btnUpdateCancel.setOnClickListener {
            alertDialog.dismiss() // Close the dialog
        }
    }

    fun updateDebtData(
        debtId:String,
        debtAmount: Double,
        debtName: String,
        debtBorrowedDate: Long,
        debtReturnDate: Long,
        debtNote: String,
        invertedBorrowedDate: Long,
        invertedReturnDate: Long
    ){
        //Initialize Firebase Auth and firebase database
        val user = Firebase.auth.currentUser
        val uid = user?.uid
        if (uid != null) {
            val dbRef = FirebaseDatabase.getInstance().getReference(uid) //initialize database with uid as the parent
            val debtInfo = DebtModel(debtId, debtAmount, debtName, debtBorrowedDate, debtReturnDate, debtNote, invertedBorrowedDate, invertedReturnDate)
            dbRef.child(debtId).setValue(debtInfo)
        }
    }
}