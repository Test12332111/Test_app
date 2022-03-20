package com.test.application.utils

import java.net.UnknownHostException

object Utils {

    fun buildHttpErr( it: Throwable, defaultErr: String): String {
        return if (it is UnknownHostException) {
           "Ошибка подключения к интернету"
        } else {
            defaultErr
        }
    }


}