package com.courseudemy.organizzeclone.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.courseudemy.organizzeclone.databinding.ActivityLoginBinding
import com.courseudemy.organizzeclone.domain.user
import com.courseudemy.organizzeclone.util.SettingsFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private var authentication: FirebaseAuth? = null
    var user:user? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        validateLogin(binding.tilEmail.editText,binding.tilPass.editText)

        binding.tvForgotPass.setOnClickListener {
            startActivity(Intent(this, ForgotPassActivity::class.java))
        }
        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    fun validateLogin(email: EditText?, pass: EditText?){
        binding.btnLogin.setOnClickListener {
            if(email!!.text.isEmpty() || pass!!.text.isEmpty()){
                Toast.makeText(this, "Preencha os campos em vermelho", Toast.LENGTH_SHORT).show()
                binding.tilEmail.error = "Campo vazio"
                binding.tilPass.error = "Campo vazio"
            } else {
                binding.tilEmail.error = null
                binding.tilPass.error = null
                user = user("", email.text.toString(), pass.text.toString())
                authentication = SettingsFirebase().getFirebaseAuth()
                authentication?.signInWithEmailAndPassword(
                    user!!.email, user!!.password
                )?.addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(this, "Usuário logado", Toast.LENGTH_SHORT).show()
                    } else {
                        val erroMessage: String
                        try {
                            throw it.exception!!
                        } catch (e: FirebaseAuthInvalidUserException){
                            erroMessage = "Usuário não cadastrado."
                        } catch (e: FirebaseAuthInvalidCredentialsException){
                            erroMessage = "E-mail ou senha incorreta!"
                        } catch (e: Exception) {
                            erroMessage = "Erro ao fazer login: " + e.message
                            e.printStackTrace()
                        }
                        Toast.makeText(this, "Erro: $erroMessage", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}