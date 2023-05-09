package com.example.qapital.activities

object CategoryOptions {
    fun expenseCategory(): ArrayList<String> {
        val listExpense = ArrayList<String>()
        listExpense.add("Food/Beverage")
        listExpense.add("Transportation")
        listExpense.add("Entertainment")
        listExpense.add("Education")
        listExpense.add("Bills")
        listExpense.add("Shopping")
        listExpense.add("Communication")
        listExpense.add("Investment")
        listExpense.add("Health")
        listExpense.add("Other Expense")

        return listExpense
    }
}