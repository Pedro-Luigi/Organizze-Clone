package com.courseudemy.organizzeclone.helper

import com.courseudemy.organizzeclone.ui.HomeActivity

class DateCustom {
    //Vou pegar a data que o usuário me passar e declarar apenas mês e ano, para o FIREBASE.
    fun monthAndYear(date:String): String {
        val returnDate = date.split(" de ")
        val month = returnDate[1]
        val year = returnDate[2]
        return month+year
    }

    //Vou exibir na tela a data sem ela estar juntas!
    fun monthAndYearView(date:String): String {
        val returnDate = date.split(" de ")
        val month = returnDate[1]
        val year = returnDate[2]
        return "${month.uppercase()} $year"
    }
}