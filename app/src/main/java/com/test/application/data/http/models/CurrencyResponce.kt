package com.test.application.data.http.models

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class CurrencyResponce(
    @Json(name = "success") val success: Boolean,
    @Json(name = "timestamp") val timestamp: Long,
    @Json(name = "base") val base: String,
    @Json(name = "rates") val rates: Map<String, Double>
) : Parcelable