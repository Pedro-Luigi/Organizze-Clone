package com.courseudemy.organizzeclone.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.courseudemy.organizzeclone.R
import com.courseudemy.organizzeclone.databinding.ActivityForgotPassBinding

class ForgotPassActivity : AppCompatActivity() {

    private val binding by lazy { ActivityForgotPassBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

    }
}