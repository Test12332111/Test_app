package com.test.application.data.db.repositories

import androidx.room.*
import com.test.application.data.db.models.FavoriteCurrencyDB

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteCurrencyDB: FavoriteCurrencyDB)

    @Query("SELECT * FROM favorite_currency WHERE base = :base")
    suspend fun getFavoriteCurrencyList(base: String): List<FavoriteCurrencyDB>

    @Delete
    suspend fun delete(favoriteCurrencyDB: FavoriteCurrencyDB)

}