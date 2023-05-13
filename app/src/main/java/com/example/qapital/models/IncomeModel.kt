package com.example.qapital.models

data class IncomeModel(
    var incomeId:String? = null,
    var incomeAmount:Double? = null,
    var incomeTitle:String? = null,
    var incomeType:String? = null,
    var incomeDate:String? = null,
    var incomeNote:String? = null
)