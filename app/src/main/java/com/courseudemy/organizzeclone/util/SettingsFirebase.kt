package com.courseudemy.organizzeclone.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SettingsFirebase {
    private var authentication: FirebaseAuth? = null
    private var firebase: DatabaseReference? = null

    //retorna a instancia do FirebaseAuth
    fun getFirebaseAuth(): FirebaseAuth? {
        if (authentication == null){
            authentication = FirebaseAuth.getInstance()
        }
        return authentication
    }

    //retorna a instancia do FirebaseDatabase
    fun getFirebaseDataBase():DatabaseReference{
        if (firebase == null){
            firebase = FirebaseDatabase.getInstance().reference
        }
        return firebase as DatabaseReference
    }

}