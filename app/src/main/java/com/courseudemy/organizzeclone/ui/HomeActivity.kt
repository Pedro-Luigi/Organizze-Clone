package com.courseudemy.organizzeclone.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.courseudemy.organizzeclone.R
import com.courseudemy.organizzeclone.databinding.ActivityHomeBinding
import com.courseudemy.organizzeclone.domain.Transition
import com.courseudemy.organizzeclone.domain.User
import com.courseudemy.organizzeclone.model.ListItemCategory
import com.courseudemy.organizzeclone.util.PullDataFirebase
import com.courseudemy.organizzeclone.util.SaveUserFirebase
import com.courseudemy.organizzeclone.util.SettingsFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding
    private var authentication: FirebaseAuth? = null
    private var mDatabase: DatabaseReference? = null
    private val TAG = "ERROR"
    companion object{
        var name:String? =null
        var allExpense:Double? = null
        var allProfit:Double? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_home)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

//        binding.fabAction.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//
//            authentication = SettingsFirebase().getFirebaseAuth()
//            //TODO Mudar daqui.
//
////                    authentication?.signOut()
//        }
        authentication = SettingsFirebase().getFirebaseAuth()
        mDatabase = SettingsFirebase().getFirebaseDataBase()

        //Ouvindo dados do firebase.
//        getDataFirebase()

        //Soó funciona se eu clicar em uma view! Merda.
        binding.teste.setOnClickListener {
            PullDataFirebase().getAllProfit("name")
            PullDataFirebase().getAllProfit("allProfit")
            PullDataFirebase().getAllProfit("allExpense")
            binding.teste.text = allProfit.toString()
            binding.teste2.text = allExpense.toString()
            binding.toolbar.title = "Olá, $name"
            binding.teste.text = allProfit?.minus(allExpense!!).toString()
        }

        binding.ivExit.setOnClickListener {
            authentication?.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        openActionScreen()

    }

    //TODO SE DER CERTO, coloque a responsabilidade para a classe
    private fun getDataFirebase(){
            val firebase = mDatabase!!.child("usuarios").child(authentication?.uid.toString())
            mDatabase!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.value
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@HomeActivity,
                        "Failed to read value. ${error.toException()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

            //Busca nome do usuário.
            firebase.child("name").get().addOnSuccessListener {
                binding.toolbar.title = "Olá, ${it.value.toString()}"
            }.addOnFailureListener {
                Log.w(TAG, "Failed to read value.", error(it))
            }

            //Busca gasto total do usuário.
            firebase.child("allExpense").get().addOnSuccessListener {
                binding.teste.text = it.value.toString()
                allExpense = it.value.toString().toDouble()
            }.addOnFailureListener {
                Log.w(TAG, "Failed to read value.", error(it))
            }

            //Busca lucro total do usuário.
            firebase.child("allProfit").get().addOnSuccessListener {
                binding.teste2.text = it.value.toString()
                allProfit = it.value.toString().toDouble()
            }.addOnFailureListener {
                Log.w(TAG, "Failed to read value.", error(it))
            }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun openActionScreen(){
        binding.fabProfit.setOnClickListener {
            startActivity(Intent(this, ProfitActivity::class.java))
        }
        binding.fabExpense.setOnClickListener {
            startActivity(Intent(this, ExpenseActivity::class.java))
        }
    }
}