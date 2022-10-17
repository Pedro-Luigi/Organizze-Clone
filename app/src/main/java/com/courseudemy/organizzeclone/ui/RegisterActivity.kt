package com.courseudemy.organizzeclone.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.courseudemy.organizzeclone.databinding.ActivityRegisterBinding
import com.courseudemy.organizzeclone.domain.user
import com.courseudemy.organizzeclone.util.SettingsFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class RegisterActivity : AppCompatActivity() {

    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private var authentication: FirebaseAuth? = null
    private var user: user? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        //retornando a instancia do firebase Auth.
        authentication = SettingsFirebase().getFirebaseAuth()

        //Validando campos
        binding.btnNewAccount.setOnClickListener {
            validation(
                binding.tilNameRegister.editText,
                binding.tilEmailRegister.editText,
                binding.tilPasswordRegister.editText
            )
        }
    }


    private fun validation(
        name: EditText?, email: EditText?, pass: EditText?
    ) {
        val symbols = Regex("[@/*!:#%¨&_+]")
        val letterUpper = Regex("[A-Z]")
        val letterLower = Regex("[a-z]")
        val numbers = Regex("[0-9]")

        if (name!!.text.isEmpty() || email!!.text.isEmpty() ||
            pass!!.text.isEmpty() || pass.text.length <= 7
        ) {
            Toast.makeText(this, "Preencha os campos em vermelho", Toast.LENGTH_SHORT).show()
            if (name.text.isEmpty()) name.error = "Campo vazio"
            if (email!!.text.isEmpty()) email.error = "Campo vazio"
            if (pass!!.text.isEmpty()) pass.error = "Campo vazio"
            if (pass.text.length <= 8) pass.error = "Senha fraca!"

        } else if (pass.text.contains(symbols) && pass.text.contains(letterUpper) &&
            pass.text.contains(letterLower) && pass.text.contains(numbers)
        ) {
            user = user(
                binding.tilNameRegister.editText?.text.toString(),
                binding.tilEmailRegister.editText?.text.toString(),
                binding.tilPasswordRegister.editText?.text.toString()
            )
            name.error = null
            email.error = null
            pass.error = null
            authentication?.createUserWithEmailAndPassword(
                user!!.email,
                user!!.password
            )?.addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(this, LoginActivity::class.java))
                } else {
                    val erroMessage: String
                    try {
                        throw it.exception!!
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        erroMessage = "Digite uma senha mais forte!"
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        erroMessage = "Por favor, digite um e-mail válido"
                    } catch (e: FirebaseAuthUserCollisionException) {
                        erroMessage = "Essa conta já foi cadastrada"
                    } catch (e: Exception) {
                        erroMessage = "ao cadastrar usuário: " + e.message
                        e.printStackTrace()
                    }
                    Toast.makeText(this, "Erro: $erroMessage", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            pass.error = "Senha precisar conter números,letras (maiúsculas e minúsculas) e símbolos!"
        }
    }
}