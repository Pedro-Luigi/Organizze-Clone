package com.courseudemy.organizzeclone.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.courseudemy.organizzeclone.R
import com.courseudemy.organizzeclone.databinding.ItemRvBinding
import com.courseudemy.organizzeclone.domain.Transition
import com.courseudemy.organizzeclone.domain.TypeTransition

class TransitionAdapter(private val transition: List<Transition>): RecyclerView.Adapter<TransitionAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding:ItemRvBinding):RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(item:Transition){
            with(itemView){
                binding.tvDate.text = item.date
                if (item.type == TypeTransition.PROFIT.toString()){
                    binding.tvValue.text = "R$"+item.value.toString()
                    binding.tvValue.setTextColor(resources.getColor(R.color.ColorProfit))
                }
                if (item.type == TypeTransition.EXPENSE.toString()){
                    binding.tvValue.text = "R$-"+item.value.toString()
                    binding.tvValue.setTextColor(resources.getColor(R.color.ColorExpense))
                }
                binding.chipCategory.text = item.category
                binding.tvDesc.text = item.description
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemRvBinding.inflate(inflater, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(transition[position])
    }

    override fun getItemCount() = transition.size
}