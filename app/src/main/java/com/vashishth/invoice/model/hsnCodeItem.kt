package com.vashishth.invoice.model

import com.vashishth.invoice.utils.autoComplete.AutoCompleteEntity
import java.util.*

data class hsnCodeItem(
    val Code: String,
    val Description: String
) : AutoCompleteEntity {
    override fun filter(query: String): Boolean {
        return Description.lowercase(Locale.getDefault())
            .startsWith(query.lowercase(Locale.getDefault()))
    }
}

data class unit(
    val unit: String
) :AutoCompleteEntity{
    override fun filter(query: String): Boolean {
        return unit.lowercase(Locale.getDefault())
            .startsWith(query.lowercase(Locale.getDefault()))
    }
}