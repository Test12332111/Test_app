package com.test.application.data

import com.test.application.data.db.repositories.CurrencyRepository
import com.test.application.data.http.HttpRestHelper
import com.test.application.data.ui.models.CurrencyUI
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.io.IOException

class CurrencyService(
    val currencyRepository: CurrencyRepository,
    val httpRestHelper: HttpRestHelper
) {

    val handleFavoriteChannel =
        MutableSharedFlow<CurrencyUI>(onBufferOverflow = BufferOverflow.SUSPEND)

    suspend fun getCurrencyList(base: String, symbols: Array<String>? = null): List<CurrencyUI> {
        return coroutineScope {
            val httpResult =
                withContext(Dispatchers.IO) { httpRestHelper.getCurrency(base, symbols) }
            val dbResult =
                withContext(Dispatchers.IO) { currencyRepository.getFavoriteCurrencyList(base) }.map { it.secondCurrency }
                    .toSet()
            if (httpResult.isSuccessful) {
                httpResult.body()!!.rates.map {
                    CurrencyUI(
                        base,
                        it.key,
                        it.value,
                        dbResult.contains(it.key)
                    )
                }
            } else {
                throw IOException(httpResult.errorBody().toString())
            }
        }
    }

    suspend fun getFavoriteList(base: String): List<CurrencyUI> {
        return currencyRepository.getFavoriteCurrencyList(base).let {
            if (it.isEmpty()) {
                return@let emptyList()
            }

            val responseList =
                httpRestHelper.getCurrency(base, it.map { it.secondCurrency }.toTypedArray())
            if (responseList.isSuccessful) {
                responseList.body()!!.rates.map {
                    CurrencyUI(
                        base,
                        it.key,
                        it.value,
                        true
                    )
                }
            } else {
                throw IOException(responseList.errorBody().toString())
            }
        }
    }

    suspend fun handleFavorite(favorite: Boolean, currencyUI: CurrencyUI): CurrencyUI {
        return if (favorite) {
            currencyRepository.addFavorite(currencyUI)
        } else {
            currencyRepository.deleteFavorite(currencyUI)
        }.let {
            currencyUI.copy(isFavorite = favorite).apply {
                GlobalScope.launch(Dispatchers.Main) {
                    handleFavoriteChannel.emit(this@apply)
                }
            }
        }
    }


}