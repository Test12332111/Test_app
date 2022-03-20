package com.test.application.injection.component

import com.test.application.injection.module.*
import com.test.application.ui.activities.main.MainViewModel
import com.test.application.ui.fragments.currency.CurrencyViewModel
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(
    modules = arrayOf(
        ApplicationModule::class,
        DbModule::class,
        MoshiModule::class,
        HttpModule::class
    )
)
interface ApplicationComponent {

    fun inject(viewModel: MainViewModel)

    fun inject(viewModel: CurrencyViewModel)


}
