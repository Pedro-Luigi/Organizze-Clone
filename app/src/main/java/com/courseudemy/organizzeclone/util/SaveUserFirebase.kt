package com.courseudemy.organizzeclone.util

import com.courseudemy.organizzeclone.domain.SaveUserDB

class SaveUserFirebase {
    fun saveUser(name: String, email: String){
        val newUser = SaveUserDB(email= email, name = name)
        //Salvando as informações do usuário.
        val firebase = SettingsFirebase().getFirebaseDataBase()
        firebase.child("usuarios")
            //Estou pegando o uid do usuário e atribuindo a sua chave no firebase e salvando seu email e nome.
            .child(SettingsFirebase().getFirebaseAuth()?.uid.toString())
            .setValue(newUser)
    }
}