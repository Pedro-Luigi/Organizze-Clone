package com.courseudemy.organizzeclone.domain

data class SaveUserDB(
    val email:String? = null,
    val name:String? = null,
    val allProfit:Double,
    val allExpense:Double
)
