package com.courseudemy.organizzeclone.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.courseudemy.organizzeclone.R
import com.courseudemy.organizzeclone.databinding.ActivityForgotPassBinding
import com.courseudemy.organizzeclone.util.SettingsFirebase
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class ForgotPassActivity : AppCompatActivity() {

    private val binding by lazy { ActivityForgotPassBinding.inflate(layoutInflater) }
    private var authentication: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        resetPass()
    }

    fun resetPass(){
        binding.btnForgotpass.setOnClickListener {
            val email = binding.tilEmailForgotpass.editText?.text.toString()
            authentication = SettingsFirebase().getFirebaseAuth()
            authentication?.sendPasswordResetEmail( email )?.addOnCompleteListener {
                if (it.isSuccessful){
                    Snackbar.make(this, binding.btnForgotpass,"Link para recuperação enviado para o email: $email",Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(this, binding.btnForgotpass,"Ocorreu um erro.",Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}