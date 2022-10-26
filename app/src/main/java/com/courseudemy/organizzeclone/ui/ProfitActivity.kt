package com.courseudemy.organizzeclone.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import com.courseudemy.organizzeclone.R
import com.courseudemy.organizzeclone.databinding.ActivityProfitBinding
import com.courseudemy.organizzeclone.domain.Transition
import com.courseudemy.organizzeclone.domain.TypeTransition
import com.courseudemy.organizzeclone.model.ListItemCategory
import com.courseudemy.organizzeclone.util.PullDataFirebase
import com.courseudemy.organizzeclone.util.SaveUserFirebase
import com.google.android.material.datepicker.MaterialDatePicker

class ProfitActivity : AppCompatActivity() {

    private val binding by lazy { ActivityProfitBinding.inflate(layoutInflater) }
    companion object{
        var allProfit:Double? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Inicia os editText com valores.
        fillEditText()
        //Escuta qual é o valor atual de allProfit no firebase
        PullDataFirebase().getAllValues("allProfit")
        //Função de cliques
        clickViews()
    }

    @SuppressLint("SetTextI18n")
    private fun clickViews() {
        binding.tietDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .build()
            datePicker.addOnPositiveButtonClickListener {
                 binding.tilProfitDate.editText?.setText(datePicker.headerText)
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        binding.fabConfirmProfit.setOnClickListener {
            validateFields(binding.edtProfit)
            validateFields(binding.actvCategory)
            validateFields(binding.tilProfitDesc.editText)
            validateFields(binding.tilProfitDate.editText)
            checkEditText()
        }
    }

    private fun fillEditText(){
        val adapter = ArrayAdapter(this, R.layout.list_item_til, R.id.tv_item, ListItemCategory.itemsProfit())
        (binding.tilCategory.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun validateFields(view: EditText?):Boolean{
        return when {
            view!!.text.isEmpty() -> {
                view.error = "Campo vazio!"
                return false
            }
            view.text.isBlank() -> {
                view.error = "Campo em branco!"
                return false
            }
            else -> return true
        }
    }

    private fun checkEditText(){
        val value = validateFields(binding.edtProfit)
        val category = validateFields(binding.tilCategory.editText)
        val desc = validateFields(binding.tilProfitDesc.editText)
        val date = validateFields(binding.tilProfitDate.editText)

        if (value && category && desc && date &&
            binding.edtProfit.text.toString().toDouble() > 0 ){
            saveProfit()
            PullDataFirebase().getAllValues("name")
            PullDataFirebase().getAllValues("allProfit")
            PullDataFirebase().getAllValues("allExpense")

            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, "Preencha os campos em vermelho ou adicone um valor acima de 0.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveProfit(){
        val transition = Transition(
            value = binding.edtProfit.text.toString().toDouble(),
            category = binding.tilCategory.editText?.text.toString(),
            description = binding.tilProfitDesc.editText?.text.toString(),
            date = binding.tilProfitDate.editText?.text.toString(),
            type = TypeTransition.PROFIT.toString()
        )
        //Soma e salva o valor lá no firebase
        allProfit = allProfit?.plus(transition.value)
        SaveUserFirebase().saveValue("allProfit", allProfit)
        SaveUserFirebase().saveTransition(transition)
    }
}
