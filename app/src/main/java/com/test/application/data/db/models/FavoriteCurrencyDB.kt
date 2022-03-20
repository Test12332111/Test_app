package com.test.application.data.db.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_currency", indices = [Index("base")])
data class FavoriteCurrencyDB(
    @PrimaryKey var id: String,
    val base: String,
    val secondCurrency: String
)