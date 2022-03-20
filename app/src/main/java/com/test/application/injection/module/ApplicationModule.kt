package com.test.application.injection.module


import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.squareup.moshi.Moshi
import com.test.application.data.CurrencyService
import com.test.application.data.db.repositories.CurrencyRepository
import com.test.application.data.http.HttpRestHelper
import com.test.application.data.local.GlobalSettings
import com.test.application.eventbus.EventBus
import com.test.application.injection.qualifier.ApplicationContext

import dagger.Module
import dagger.Provides

import javax.inject.Singleton


@Module
class ApplicationModule(private val mApplication: Application) {

    @Provides
    fun provideApplication(): Application {
        return mApplication
    }

    @Provides
    @ApplicationContext
    fun provideContext(): Context {
        return mApplication.applicationContext
    }

    @Provides
    @Singleton
    fun provideResources(): Resources {
        return mApplication.resources
    }

    @Provides
    @Singleton
    fun provideGlobalSettings(@ApplicationContext context: Context, moshi: Moshi): GlobalSettings {
        return GlobalSettings(context, moshi)
    }

    @Provides
    @Singleton
    fun provideEventBus(): EventBus {
        return EventBus()
    }

    @Provides
    @Singleton
    fun provideCurrencyService(currencyRepository: CurrencyRepository, httpRestHelper: HttpRestHelper): CurrencyService {
        return CurrencyService(currencyRepository, httpRestHelper)
    }


}
