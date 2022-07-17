package com.vashishth.invoice.model

import androidx.annotation.DrawableRes

data class Page(
    val title : String,
    val description : String,
    @DrawableRes val image : Int
)
