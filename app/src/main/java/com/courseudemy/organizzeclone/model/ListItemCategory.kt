package com.courseudemy.organizzeclone.model

object ListItemCategory {
    fun itemsExpense(): List<String> {
        val items = listOf(
            "Moradia", "Alimentação", "Transporte",
            "Saúde", "Educação", "Lazer", "Outros"
        )
        return items.sorted()
    }

    fun itemsProfit(): List<String> {
        val items = listOf(
            "Salário", "Vendas", "Prestação de serviço",
            "Aluguel", "Dividendo", "Outros"
        )
        return items.sorted()
    }
}