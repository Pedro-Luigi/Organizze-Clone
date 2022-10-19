package com.courseudemy.organizzeclone.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.courseudemy.organizzeclone.R
import com.courseudemy.organizzeclone.databinding.ActivityProfitBinding

class ProfitActivity : AppCompatActivity() {

    private val binding by lazy { ActivityProfitBinding.inflate(layoutInflater) }
    private val items = listOf("Material", "Design", "Components", "Android")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val adapter = ArrayAdapter(this, R.layout.list_item_til, R.id.tv_item, items)
        (binding.tilCategory.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }
}