package com.courseudemy.organizzeclone.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.courseudemy.organizzeclone.R
import com.courseudemy.organizzeclone.databinding.ActivityHomeBinding
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
        getDataFirebase()

        binding.ivExit.setOnClickListener {
            authentication?.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        openActionScreen()

    }

    fun getDataFirebase(){
        mDatabase!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        mDatabase!!.child("usuarios").child(authentication?.uid.toString())
            .child("name").get().addOnSuccessListener {
                binding.toolbar.title = "Olá, ${it.value.toString()}"
            }.addOnFailureListener {
                binding.toolbar.title = "Olá"
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