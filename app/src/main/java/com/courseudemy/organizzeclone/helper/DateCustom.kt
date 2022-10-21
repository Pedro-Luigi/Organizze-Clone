package com.courseudemy.organizzeclone.helper

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat

class DateCustom {

    @SuppressLint("SimpleDateFormat")
    fun dateNow(): String {
        val date = System.currentTimeMillis()
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        return simpleDateFormat.format(date).toString()
    }

    //Vou pegar a data que o usuário me passar e clarar apenas mês e ano.
    fun monthAndYear(date:String): String {
        val returnDate = date.split("/")
        val month = returnDate[1]
        val year = returnDate[2]

        return month+year
    }

}