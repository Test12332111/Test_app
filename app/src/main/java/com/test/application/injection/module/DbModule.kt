package com.test.application.injection.module

import android.content.Context
import androidx.room.Room
import com.test.application.data.db.CurrencyDatabase
import com.test.application.data.db.repositories.CurrencyRepository
import dagger.Module
import dagger.Provides
import com.test.application.injection.qualifier.ApplicationContext
import javax.inject.Singleton

@Module
class DbModule {

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context): CurrencyDatabase {
        return Room.databaseBuilder(context,
            CurrencyDatabase::class.java, "favorite_currency.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideTrackRepository(db:  CurrencyDatabase): CurrencyRepository {
        return CurrencyRepository(db.currencyDao())
    }
}