package com.test.application.data.db


import androidx.room.Database
import androidx.room.RoomDatabase
import com.test.application.data.db.models.FavoriteCurrencyDB
import com.test.application.data.db.repositories.CurrencyDao

@Database(entities = [FavoriteCurrencyDB::class], version = 1, exportSchema = false)
abstract class CurrencyDatabase : RoomDatabase() {

    abstract fun currencyDao() : CurrencyDao



}