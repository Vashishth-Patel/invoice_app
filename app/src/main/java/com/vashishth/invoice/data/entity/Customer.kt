package com.vashishth.invoice.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vashishth.invoice.utils.autoComplete.AutoCompleteEntity
import java.util.*

@Entity
data class Customer(
    val name: String,
    val phoneNumber: Long?,
    val email: String?
) : AutoCompleteEntity {
    override fun filter(query: String): Boolean {
        return name.lowercase(Locale.getDefault())
            .startsWith(query.lowercase(Locale.getDefault()))
    }

    @PrimaryKey(autoGenerate = true)
    var  customerRegNo: Int = 0
}
