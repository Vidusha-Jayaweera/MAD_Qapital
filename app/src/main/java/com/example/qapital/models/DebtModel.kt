package com.example.qapital.models

data class DebtModel (
    var debtId: String? = null,
    var debtAmount: Double? = null,
    var debtName: String? = null,
    var debtBorrowedDate: Long? = null,
    var debtReturnDate: Long? = null,
    var debtNote: String? = null,
    var invertedBorrowedDate: Long?=null,
    var invertedReturnDate: Long?=null,
    var payStatus: String?=null,
)