package com.test.application.data.local

import android.content.Context
import com.squareup.moshi.Moshi
import com.test.application.data.http.models.CurrencyResponce
import java.util.concurrent.ConcurrentHashMap


class GlobalSettings(
    val context: Context,
    val moshi: Moshi
) {

    val preferences = context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)

    val NULL_MARKER = Any()
    val values = ConcurrentHashMap<String, Any>()


    var lastCurrencyResponceList: CurrencyResponce?
        get() = getDataFromCache<CurrencyResponce>(CACHE_LAST_CURRENCY_LIST)
        set(value) {
            updateCache(value, CACHE_LAST_CURRENCY_LIST)
        }

    var currentCurrency: String?
        get() = getDataFromCache<String>(CACHE_CURRENT_CURRENCY)
        set(value) {
            updateCache(value, CACHE_CURRENT_CURRENCY)
        }



    inline fun <reified T> updateCache(body: T, key: String) {
        val adapter = moshi.adapter<T>(T::class.java)
        val jsonString = adapter.toJson(body)
        val editor = preferences.edit()
        editor.putString(key, jsonString)
        editor.apply()
        values[key] = (body ?: NULL_MARKER)
    }

    inline fun <reified T> getDataFromCache(type: String): T? {
        return if (values.contains(type)) {
            val value = values[type]
            if (value == NULL_MARKER) {
                null
            } else {
                values[type] as T?
            }

        } else {
            val adapter = moshi.adapter<T>(T::class.java)
            val json = preferences.getString(type, null)
            return try {
                adapter.fromJson(json)
            } catch (e: Exception) {
                null
            }
        }
    }


    companion object {

        private val SHARED_PREFERENCE = "test.prefer"

        const val CACHE_CURRENT_CURRENCY = "CACHE_CURRENT_CURRENCY"
        const val CACHE_LAST_CURRENCY_LIST = "CACHE_LAST_CURRENCY_LIST"

    }

}