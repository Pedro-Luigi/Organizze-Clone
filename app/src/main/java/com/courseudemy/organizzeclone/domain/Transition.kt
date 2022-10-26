package com.courseudemy.organizzeclone.domain

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Transition(
    val category:String,
    val description:String,
    val date:String,
    val type:String,
    val value:Double
){
    var key: String = ""

    constructor(category:String, description:String, date:String,
                type:String, value:Double, key:String) : this(category, description, date, type, value){
            this.key = key
        }
}
