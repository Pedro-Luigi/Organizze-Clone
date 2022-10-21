package com.courseudemy.organizzeclone.ui

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
import com.courseudemy.organizzeclone.helper.DateCustom
import com.courseudemy.organizzeclone.model.ListItemCategory
import com.courseudemy.organizzeclone.util.PullDataFirebase
import com.courseudemy.organizzeclone.util.SaveUserFirebase

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
        PullDataFirebase().getAllProfit("allProfit")

        binding.fabConfirmProfit.setOnClickListener {
            validateFields(binding.edtProfit)
            validateFields(binding.actvCategory)
            validateFields(binding.tilProfitDesc.editText)
            validateFields(binding.tilProfitDate.editText)
            checkEditText()
        }
    }

    private fun fillEditText(){
        binding.tilProfitDate.editText?.setText(DateCustom().dateNow())
        val adapter = ArrayAdapter(this, R.layout.list_item_til, R.id.tv_item, ListItemCategory.items())
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
