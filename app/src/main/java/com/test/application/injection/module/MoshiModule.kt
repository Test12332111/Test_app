package com.test.application.injection.module

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.JsonReader.Token.NULL
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Singleton


@Module
class MoshiModule {

    @Provides
    @Singleton
    internal fun provideMoshi(): Moshi {
        val moshiBuilder = Moshi.Builder()
        moshiBuilder.add(Date::class.java, object : JsonAdapter<Date>() {

            override fun fromJson(reader: JsonReader): Date? {
                if (reader.peek() == NULL) {
                    return reader.nextNull()
                }

                val dateAsString = reader.nextString()
                return try {
                    synchronized(dateFormat) {
                        dateFormat.parse(dateAsString)
                    }
                } catch (e: Exception) {
                    throw Exception(e)
                }
            }

            override fun toJson(writer: JsonWriter, value: Date?) {
                if (value == null) {
                    writer.nullValue()
                } else {
                    synchronized(dateFormat) {
                        writer.value(dateFormat.format(value))
                    }
                }
            }
        })

        return moshiBuilder.build()
    }

    companion object {

        const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"

        private val dateFormat = object : SimpleDateFormat(DATE_FORMAT) {
            init {
                this.isLenient = false
                this.timeZone = TimeZone.getTimeZone("UTC")
            }
        }

    }
}
