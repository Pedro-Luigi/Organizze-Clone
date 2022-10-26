package com.courseudemy.organizzeclone.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.courseudemy.organizzeclone.R
import com.courseudemy.organizzeclone.databinding.ActivityHomeBinding
import com.courseudemy.organizzeclone.domain.Transition
import com.courseudemy.organizzeclone.domain.TypeTransition
import com.courseudemy.organizzeclone.helper.DateCustom
import com.courseudemy.organizzeclone.model.ListItemCategory
import com.courseudemy.organizzeclone.ui.adapter.TransitionAdapter
import com.courseudemy.organizzeclone.util.PullDataFirebase
import com.courseudemy.organizzeclone.util.SaveUserFirebase
import com.courseudemy.organizzeclone.util.SettingsFirebase
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class HomeActivity : AppCompatActivity() {

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private var authentication: FirebaseAuth? = null
    private var mDatabase: DatabaseReference? = null
    private var generalValue = "0.00"

    companion object {
        var name: String = ""
        var allExpense: String = ""
        var allProfit: String = ""
        val listTransition = arrayListOf<Transition>()
        val adapter by lazy { TransitionAdapter(listTransition) }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

        //Conferindo se RecyclerView está vazio ou não
        emptyTransitions(listTransition)

        authentication = SettingsFirebase().getFirebaseAuth()
        mDatabase = SettingsFirebase().getFirebaseDataBase()

        //Preenchendo as View com informções
        pullData()
        fillEditText()
        //Função responsável por excluir uma transição.
        swip()
        //Carrega informações para a view ao iniciar
        loadInfo()
        //Abre telas de despesa e lucro
        openActionScreen()
        //Configura clique nas visualizações.
        clicksViews()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun emptyTransitions(list:ArrayList<Transition>){
        Handler().postDelayed({
            kotlin.runCatching {
                adapter.notifyDataSetChanged()
                if (list.isEmpty()) {
                    binding.rvTransitions.removeAllViews()
                    binding.tvNoTransitions.visibility = View.VISIBLE
                    binding.ivNoTransitions.visibility = View.VISIBLE
                } else {
                    binding.tvNoTransitions.visibility = View.GONE
                    binding.ivNoTransitions.visibility = View.GONE
                }
            }
        }, 200)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clicksViews() {
        binding.ivLogo.setOnClickListener {
            startActivity(Intent(this, this::class.java))
        }

        binding.actvCategory.setOnItemClickListener { parent, view, position, id ->
            searceCategory()
        }

        binding.ivExit.setOnClickListener {
            authentication?.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.fabSearce.setOnClickListener {
                val datePicker = MaterialDatePicker.Builder.datePicker()
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                    .build()
                datePicker.addOnPositiveButtonClickListener {
                    PullDataFirebase().listenerTransitionsWithDate(
                        DateCustom().monthAndYear(datePicker.headerText)
                    )
                    binding.tvDate.text = DateCustom().monthAndYearView(datePicker.headerText)
                    binding.tvDate.visibility = View.VISIBLE
                    emptyTransitions(listTransition)
                }
                datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }
    }

    private fun fillEditText(){
        //Preenche lista de categorias.
        val adapter = ArrayAdapter(this, R.layout.list_item_til, R.id.tv_item, ListItemCategory.itemsProfit() + ListItemCategory.itemsExpense())
        (binding.chipSourceCategory.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun searceCategory(){
        val listCategory = arrayListOf<Transition>()
        listTransition.forEach {
            if (it.category == binding.chipSourceCategory.editText?.text.toString()){
                listCategory.add(it)
            }
            //Essa função confere se lista está vazia.
            emptyTransitions(listCategory)
        }
        binding.rvTransitions.adapter = TransitionAdapter(listCategory)
        adapter.notifyDataSetChanged()
    }

    private fun loadInfo() {
        Handler().postDelayed({
            kotlin.run {
                PullDataFirebase().getAllValues("name")
                PullDataFirebase().getAllValues("allProfit")
                PullDataFirebase().getAllValues("allExpense")
                //listando transições e recuperando key de movimentação do firebase
                PullDataFirebase().listenerTransitions()
                binding.rvTransitions.adapter = adapter
                Log.i("START", "EVENTO ADICIONADO!")
                pullData()
                emptyTransitions(listTransition)
            }
        }, 500)
    }

    fun swip() {
        val itemTouch: ItemTouchHelper.Callback =
            object : ItemTouchHelper.Callback() {
                override fun getMovementFlags(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ): Int {
                    //Faz com que o movimento DragAndDrop esteja inativo, apenas para os lados.
                    val dragFlag = ItemTouchHelper.ACTION_STATE_IDLE
                    val swipeFlags = ItemTouchHelper.END
                    return makeMovementFlags(dragFlag, swipeFlags)
                }

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    deleteTransition(viewHolder)
                }
            }
        ItemTouchHelper(itemTouch).attachToRecyclerView(binding.rvTransitions)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteTransition(viewHolder: RecyclerView.ViewHolder) {
        //Abre um AlertDialog
        MaterialAlertDialogBuilder(this)
            .setTitle("Excluir movimentação")
            .setMessage("Você tem certeza que deseja excluir movimentação?")
            .setCancelable(false)
            .setNegativeButton("Cancelar") { dialog, which ->
                adapter.notifyDataSetChanged()
            }
            .setPositiveButton("Confirmar") { dialog, which ->
                val position = viewHolder.adapterPosition
                val transition = listTransition[position]
                SaveUserFirebase().deleteValueTransition(transition.date, transition.key)
                adapter.notifyItemRemoved(position)
                if (transition.type == TypeTransition.PROFIT.toString()) {
                    allProfit = (allProfit.toDouble().minus(transition.value)).toString()
                    mDatabase!!.child("usuarios").child(authentication?.uid.toString())
                        .child("allProfit").setValue(allProfit)
                    pullData()
                }
                if (transition.type == TypeTransition.EXPENSE.toString()) {
                    allExpense = (allExpense.toDouble().minus(transition.value)).toString()
                    mDatabase!!.child("usuarios").child(authentication?.uid.toString())
                        .child("allExpense").setValue(allExpense)
                    pullData()
                }
                pullData()
            }
            .show()
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun pullData() {
        binding.tvUserName.text = "Olá, $name"
        if (allProfit.isNotEmpty() && allExpense.isNotEmpty()) {
            generalValue = (allProfit.toDouble().minus(allExpense.toDouble()).convert())
            binding.tvBalance.text = generalValue
            adapter.notifyDataSetChanged()
            binding.progressCircular.visibility = View.GONE
        } else {
            binding.progressCircular.visibility = View.VISIBLE
            loadInfo()
        }
    }

    private fun Double.convert(): String {
        val format = DecimalFormat("#,##0.00")
        format.isDecimalSeparatorAlwaysShown = false
        return format.format(this).toString()
    }

    private fun openActionScreen() {
        binding.fabProfit.setOnClickListener {
            startActivity(Intent(this, ProfitActivity::class.java))
        }
        binding.fabExpense.setOnClickListener {
            startActivity(Intent(this, ExpenseActivity::class.java))
        }
    }

    override fun onStop() {
        super.onStop()
        Log.i("onStop", "EVENTO REMOVIDO!")
        PullDataFirebase().stopListener()
    }
}