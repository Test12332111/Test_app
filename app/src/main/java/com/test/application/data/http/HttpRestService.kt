package com.test.application.data.http

import com.test.application.data.http.models.CurrencyResponce
import retrofit2.Response
import retrofit2.http.*

interface HttpRestService {


    @GET("latest")
    suspend fun getCurrency(@QueryMap options: Map<String, String>) : Response<CurrencyResponce>


}