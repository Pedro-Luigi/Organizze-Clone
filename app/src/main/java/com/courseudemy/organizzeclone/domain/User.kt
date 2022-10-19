package com.courseudemy.organizzeclone.domain

import com.google.firebase.database.Exclude

data class User(
    val name: String,
    val email: String,
    @Exclude
    val password:String
)
