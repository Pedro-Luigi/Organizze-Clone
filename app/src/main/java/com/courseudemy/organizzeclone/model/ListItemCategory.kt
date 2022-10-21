package com.courseudemy.organizzeclone.model

object ListItemCategory {
    fun items(): List<String>{
        val items = listOf("Moradia", "Alimentação", "Transporte",
            "Saúde", "Educação", "Lazer", "Outros")
        return items.sorted()
    }
}