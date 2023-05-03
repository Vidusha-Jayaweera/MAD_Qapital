package com.example.qapital.model

data class DebtModel (
    var debtId: String? = null,
    var debtAmount: String? = null,
    var debtName: String? = null,
    var debtBorrowedDate: Long? = null,
    var debtReturnDate: Long? = null,
    var debtNote: String? = null
)