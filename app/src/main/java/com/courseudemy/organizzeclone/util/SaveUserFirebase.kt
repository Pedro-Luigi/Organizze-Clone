package com.courseudemy.organizzeclone.util

import com.courseudemy.organizzeclone.domain.SaveUserDB
import com.courseudemy.organizzeclone.domain.Transition
import com.courseudemy.organizzeclone.helper.DateCustom

class SaveUserFirebase {
    private val firebase = SettingsFirebase().getFirebaseDataBase()

    fun saveUser(name: String, email: String){
        val newUser = SaveUserDB(email= email, name = name, 0.0,0.0)
        //Salvando as informações do usuário.
        firebase.child("usuarios")
            //Estou pegando o uid do usuário e atribuindo a sua chave no firebase e salvando seu email e nome.
            .child(SettingsFirebase().getFirebaseAuth()?.uid.toString())
            .setValue(newUser)
    }

    fun saveTransition(transition: Transition){
        val newTransition = Transition(
            transition.category,
            transition.description,
            transition.date,
            transition.type,
            transition.value
        )
        firebase.child("movimentacao")
            .child(SettingsFirebase().getFirebaseAuth()?.uid.toString())
            .child(DateCustom().monthAndYear(transition.date))
            .push()
            .setValue(newTransition)
    }

    fun saveValue(type: String, value: Double?){
        firebase.child("usuarios")
            .child(SettingsFirebase().getFirebaseAuth()?.uid.toString())
            .child(type)
            .setValue(value)
    }
}