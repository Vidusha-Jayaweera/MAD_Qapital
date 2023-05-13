package com.example.qapital.models

data class ExpenseModel(
    var expenseId:String? = null,
    var expenseAmount:Double? = null,
    var expenseTitle:String? = null,
    var expenseCategory:String? = null,
    var expenseDate:String? = null,
    var expenseDescription:String? = null
)