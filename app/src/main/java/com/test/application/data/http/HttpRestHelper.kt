package com.test.application.data.http

import com.test.application.data.http.models.CurrencyResponce
import retrofit2.Response
import java.util.HashMap


class HttpRestHelper(private val service: HttpRestService) {

    suspend fun getCurrency(base: String, symbols: Array<String>? = null): Response<CurrencyResponce> {
        val queryParams = object : HashMap<String, String>() {
            init {
                put("base", base)
                symbols?.let {
                    put("symbols", it.joinToString())
                }
            }
        }

        return service.getCurrency(queryParams)
    }

}