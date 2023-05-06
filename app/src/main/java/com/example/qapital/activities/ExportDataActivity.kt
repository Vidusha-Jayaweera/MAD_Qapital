package com.example.qapital.activities

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.qapital.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook

class ExportDataActivity : AppCompatActivity() {

    private var dateStart: Long = 0
    private var dateEnd: Long = 0

    private var TAG: String = "ExcelUtil"
    private lateinit var cell: Cell
    private lateinit var workbook: Workbook
    private lateinit var sheet: Sheet
    private lateinit var headerCellStyle: CellStyle

    // Initialize Firebase Database
    private var dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference()

    private val tagPermission: String? = ExportDataActivity::class.java.simpleName
    private val PERMISSIONS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private lateinit var fileName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_export_data)
    }
}