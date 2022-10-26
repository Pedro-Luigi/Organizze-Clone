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
import com.courseudemy.organizzeclone.databinding.ActivityExpenseBinding
import com.courseudemy.organizzeclone.domain.Transition
import com.courseudemy.organizzeclone.domain.TypeTransition
import com.courseudemy.organizzeclone.model.ListItemCategory
import com.courseudemy.organizzeclone.util.PullDataFirebase
import com.courseudemy.organizzeclone.util.SaveUserFirebase
import com.google.android.material.datepicker.MaterialDatePicker

class ExpenseActivity : AppCompatActivity() {

    private val binding by lazy { ActivityExpenseBinding.inflate(layoutInflater) }
    companion object{
        var allExpense:Double? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Inicia os editText com valores.
        fillEditText()
        //Escuta qual é o valor atual de allExpense no firebase
        PullDataFirebase().getAllValues("allExpense")
        //Função de cliques
        clickViews()
    }

    @SuppressLint("SetTextI18n")
    fun clickViews(){
        binding.tietDate.setOnClickListener{
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .build()
            datePicker.addOnPositiveButtonClickListener {
                  binding.tilExpenseDate.editText?.setText(datePicker.headerText)
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        binding.fabConfirmExpense.setOnClickListener {
            validateFields(binding.edtExpense)
            validateFields(binding.actvCategory)
            validateFields(binding.tilExpenseDesc.editText)
            validateFields(binding.tilExpenseDate.editText)
            checkEditText()
        }
    }

    private fun fillEditText(){
        val adapter = ArrayAdapter(this, R.layout.list_item_til, R.id.tv_item, ListItemCategory.itemsExpense())
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
        val value = validateFields(binding.edtExpense)
        val category = validateFields(binding.tilCategory.editText)
        val desc = validateFields(binding.tilExpenseDesc.editText)
        val date = validateFields(binding.tilExpenseDate.editText)

        if (value && category && desc && date &&
            binding.edtExpense.text.toString().toDouble() > 0 ){
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
            category = binding.tilCategory.editText?.text.toString(),
            value = binding.edtExpense.text.toString().toDouble(),
            description = binding.tilExpenseDesc.editText?.text.toString(),
            date = binding.tilExpenseDate.editText?.text.toString(),
            type = TypeTransition.EXPENSE.toString()
        )
        //Soma e salva o valor lá no firebase
        allExpense = allExpense?.plus(transition.value)
        SaveUserFirebase().saveValue("allExpense", allExpense)
        SaveUserFirebase().saveTransition(transition)
    }
}