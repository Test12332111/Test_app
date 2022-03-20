package com.test.application.data.db.repositories

import com.test.application.data.db.models.FavoriteCurrencyDB
import com.test.application.data.ui.models.CurrencyUI

class CurrencyRepository(val db: CurrencyDao) {


    suspend fun addFavorite(currencyUI: CurrencyUI) {
        db.insert(FavoriteCurrencyDB(currencyUI.id, currencyUI.base, currencyUI.secondCurrency))
    }

    suspend fun deleteFavorite(currencyUI: CurrencyUI) {
        db.delete(FavoriteCurrencyDB(currencyUI.id, currencyUI.base, currencyUI.secondCurrency))
    }

    suspend fun getFavoriteCurrencyList(base: String): List<FavoriteCurrencyDB> {
        return db.getFavoriteCurrencyList(base)
    }


}