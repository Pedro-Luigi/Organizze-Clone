package com.courseudemy.organizzeclone.util

import android.util.Log
import com.courseudemy.organizzeclone.model.ListItemCategory
import com.courseudemy.organizzeclone.ui.ExpenseActivity
import com.courseudemy.organizzeclone.ui.HomeActivity
import com.courseudemy.organizzeclone.ui.ProfitActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class PullDataFirebase {
    private var authentication: FirebaseAuth? = null
    private var mDatabase:DatabaseReference? = null

    fun getAllProfit(path:String) {
        authentication = SettingsFirebase().getFirebaseAuth()
        mDatabase = SettingsFirebase().getFirebaseDataBase()
        val pathFirebase = mDatabase!!.child("usuarios").child(authentication?.uid.toString())

        object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.child("usuarios").child(authentication?.uid.toString()).value
            }
            override fun onCancelled(error: DatabaseError) {
                //Preciso tratar melhor, caso ele nao encontre o path.
                Log.w("ERROR", "loadPost:onCancelled", error.toException())
            }
        }
        if (path == "name"){
            pathFirebase.child(path).get().addOnSuccessListener {
                HomeActivity.name = it.value.toString()
            }
        }
        if (path == "allProfit"){
            pathFirebase.child(path).get().addOnSuccessListener {
                ProfitActivity.allProfit = it.value.toString().toDouble()
                HomeActivity.allProfit = it.value.toString().toDouble()
            }
        }
        if (path == "allExpense"){
            pathFirebase.child(path).get().addOnSuccessListener {
                ExpenseActivity.allExpense = it.value.toString().toDouble()
                HomeActivity.allExpense = it.value.toString().toDouble()
            }
        }
    }


}