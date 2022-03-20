package com.test.application.data.ui.models

data class CurrencyUI(val base: String, val secondCurrency: String,  val rate: Double, val isFavorite : Boolean) {

    val id: String
        get() = "${base}:${secondCurrency}"

}


