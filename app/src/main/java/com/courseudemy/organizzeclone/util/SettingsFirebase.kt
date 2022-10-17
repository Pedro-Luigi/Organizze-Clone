package com.courseudemy.organizzeclone.util

import com.google.firebase.auth.FirebaseAuth

class SettingsFirebase {
    private var authentication: FirebaseAuth? = null

    fun getFirebaseAuth(): FirebaseAuth? {
        if (authentication == null){
            authentication = FirebaseAuth.getInstance()
        }
        return authentication
    }
}