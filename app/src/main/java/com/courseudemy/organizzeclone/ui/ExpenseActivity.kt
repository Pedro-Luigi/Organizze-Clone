package com.courseudemy.organizzeclone.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.courseudemy.organizzeclone.databinding.ActivityExpenseBinding

class ExpenseActivity : AppCompatActivity() {

    private val binding by lazy { ActivityExpenseBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



    }
}