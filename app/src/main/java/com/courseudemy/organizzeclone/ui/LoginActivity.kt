package com.courseudemy.organizzeclone.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.courseudemy.organizzeclone.databinding.ActivityLoginBinding
import com.courseudemy.organizzeclone.util.SettingsFirebase
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        validateLogin()

        binding.tvForgotPass.setOnClickListener {
            startActivity(Intent(this, ForgotPassActivity::class.java))
        }
        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    fun validateLogin(){
        binding.btnLogin.setOnClickListener {
            if(binding.tilEmail.editText!!.text.isEmpty()){
                Toast.makeText(this, "Preencha os campos em vermelho", Toast.LENGTH_SHORT).show()
                binding.tilEmail.error = "Campo vazio"
            } else {
                binding.tilEmail.error = null
            }
            if(binding.tilPass.editText!!.text.isEmpty()){
                Toast.makeText(this, "Preencha os campos em vermelho", Toast.LENGTH_SHORT).show()
                binding.tilPass.error = "Campo vazio"
            } else {
                binding.tilPass.error = null
            }
        }
    }
}