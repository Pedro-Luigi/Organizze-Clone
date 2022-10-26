package com.courseudemy.organizzeclone.util

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import com.courseudemy.organizzeclone.databinding.ActivityHomeBinding
import com.courseudemy.organizzeclone.domain.Transition
import com.courseudemy.organizzeclone.ui.ExpenseActivity
import com.courseudemy.organizzeclone.ui.HomeActivity
import com.courseudemy.organizzeclone.ui.ProfitActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class PullDataFirebase {
    private var authentication: FirebaseAuth? = null
    private var mDatabase: DatabaseReference? = null
    private var mRefTransition: DatabaseReference? = null
    private var mRefTransitionWithDate: DatabaseReference? = null
    private var valueEventListenerUser: ValueEventListener? = null

    fun getAllValues(path: String) {
        authentication = SettingsFirebase().getFirebaseAuth()
        mDatabase = SettingsFirebase().getFirebaseDataBase()
        val pathFirebase = mDatabase!!.child("usuarios").child(authentication?.uid.toString())

        valueEventListenerUser = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.child("usuarios").child(authentication?.uid.toString()).value
            }

            override fun onCancelled(error: DatabaseError) {
                //Preciso tratar melhor, caso ele nao encontre o path.
                Log.w("ERROR", "loadPost:onCancelled", error.toException())
            }
        }
        if (path == "name") {
            pathFirebase.child(path).get().addOnSuccessListener {
                HomeActivity.name = it.value.toString()
            }
        }
        if (path == "allProfit") {
            pathFirebase.child(path).get().addOnSuccessListener {
                ProfitActivity.allProfit = it.value.toString().toDouble()
                HomeActivity.allProfit = it.value.toString()
            }
        }
        if (path == "allExpense") {
            pathFirebase.child(path).get().addOnSuccessListener {
                ExpenseActivity.allExpense = it.value.toString().toDouble()
                HomeActivity.allExpense = it.value.toString()
            }
        }
    }

    fun listenerTransitions() {
        authentication = SettingsFirebase().getFirebaseAuth()
        mDatabase = SettingsFirebase().getFirebaseDataBase()
        mRefTransition = mDatabase!!.child("movimentacao").child(authentication?.uid.toString())
        val postListener = mRefTransition!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //Limpando para não ter duplicação.
                HomeActivity.listTransition.clear()
                for (transitions in snapshot.child("movimentacao")
                    .child(authentication?.uid.toString()).children) {
                    for (transition in transitions.children) {
                        HomeActivity.listTransition.add(
                            Transition(
                                transition.child("category").value.toString(),
                                transition.child("description").value.toString(),
                                transition.child("date").value.toString(),
                                transition.child("type").value.toString(),
                                transition.child("value").value.toString().toDouble(),
                                transition.key.toString()
                            )
                        )
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //Preciso tratar melhor, caso ele nao encontre o path.
                Log.w("ERROR", "loadPost:onCancelled", error.toException())
            }

        })
        mDatabase?.addValueEventListener(postListener)
    }

    fun listenerTransitionsWithDate(date: String) {
        authentication = SettingsFirebase().getFirebaseAuth()
        mDatabase = SettingsFirebase().getFirebaseDataBase()
        mRefTransitionWithDate =
            mDatabase!!.child("movimentacao").child(authentication?.uid.toString()).child(date)
        val postListener =
            mRefTransitionWithDate!!.addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    //Limpando para não ter duplicação.
                    HomeActivity.listTransition.clear()
                    for (transitions in snapshot.child("movimentacao")
                        .child(authentication?.uid.toString()).child(date).children) {
                        HomeActivity.listTransition.add(
                            Transition(
                                transitions.child("category").value.toString(),
                                transitions.child("description").value.toString(),
                                transitions.child("date").value.toString(),
                                transitions.child("type").value.toString(),
                                transitions.child("value").value.toString().toDouble(),
                                transitions.key.toString()
                            )
                        )
                        HomeActivity.adapter.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    //Preciso tratar melhor, caso ele nao encontre o path.
                    Log.w("ERROR", "loadPost:onCancelled", error.toException())
                }

            })
        mDatabase?.addValueEventListener(postListener)
    }

    fun stopListener() {
        valueEventListenerUser?.let { mDatabase?.removeEventListener(it) }
    }
}