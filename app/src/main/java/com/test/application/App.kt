package com.test.application

import android.app.Application
import android.content.Context
import com.test.application.injection.component.ApplicationComponent
import com.test.application.injection.component.DaggerApplicationComponent
import com.test.application.injection.module.ApplicationModule
import com.test.application.injection.module.DbModule
import com.test.application.injection.module.HttpModule
import com.test.application.injection.module.MoshiModule

class App : Application() {

    private lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
        context = this.applicationContext
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .dbModule(DbModule())
                .httpModule(HttpModule(BASE_URL))
                .moshiModule(MoshiModule())
                .build()
        }
    }


    companion object {

        const val BASE_URL = "http://api.exchangeratesapi.io/"
        const val API_ACCESS_KEY ="56ada189c4e4eecaac170093c6e5f1c3"


        private var mApplicationComponent: ApplicationComponent? = null

        fun getApplicationComponent(): ApplicationComponent {
            return mApplicationComponent!!
        }

        fun get(context: Context): App {
            return context.applicationContext as App
        }
    }
}