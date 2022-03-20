package com.test.application.ui.activities.main

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.application.App
import com.test.application.data.db.repositories.CurrencyRepository
import com.test.application.data.http.HttpRestHelper
import com.test.application.data.local.GlobalSettings
import com.test.application.eventbus.EventBus
import com.test.application.eventbus.events.SortCurrencyEvent
import com.test.application.eventbus.events.UpdateBaseCurrencyEvent
import com.test.application.ui.fragments.currency.CurrencyFragment
import kotlinx.coroutines.*

import javax.inject.Inject
import kotlin.collections.ArrayList


class MainViewModel : ViewModel() {


    @Inject
    lateinit var currencyRepository: CurrencyRepository

    @Inject
    lateinit var httpRestHelper: HttpRestHelper

    @Inject
    lateinit var eventBus: EventBus

    @Inject
    lateinit var globalSettings: GlobalSettings

    @Inject
    lateinit var application: Application

    val pages = ArrayList<MainPagerAdapter.PagerModel>()

    val pagesLive: MutableLiveData<List<MainPagerAdapter.PagerModel>> = MutableLiveData()

     val sortArray = arrayOf(
        Pair(SortCurrencyEvent.SORT_NAME_ASC, "Название а-я"),
        Pair(SortCurrencyEvent.SORT_NAME_DESC, "Название я-а"),
        Pair(SortCurrencyEvent.SORT_VALUE_ASC, "Курс возр."),
        Pair(SortCurrencyEvent.SORT_VALUE_DESC, "Курс убыв.")
    )


    val CURRENCY_LIST = arrayOf("EUR", /*"USD"*/)
    //API works only with EUR

    var selectedCurrency: String? = null
        private set
        get() = (field ?: globalSettings.currentCurrency).let { currency ->
            CURRENCY_LIST.firstOrNull { it == currency }
                ?: CURRENCY_LIST.first()
        }

    var selectedCurrencySort: String? = null
        private set
        get() = field ?: sortArray.first().first


    init {
        App.getApplicationComponent().inject(this)
        updateCurrencySort(selectedCurrencySort!!)
        updateBaseCurrency(selectedCurrency!!)
        updateInnerFragments()
    }

    fun updateBaseCurrency(currency: String) {
        selectedCurrency = currency
        globalSettings.currentCurrency = currency
        GlobalScope.launch(Dispatchers.Main) {
            eventBus.updateBaseCurrency.emit(UpdateBaseCurrencyEvent())
        }
    }

    fun updateCurrencySort(sort: String) {
        selectedCurrencySort = sort
        GlobalScope.launch(Dispatchers.Main) {
            eventBus.sortCurrency.emit(SortCurrencyEvent(sort))
        }
    }

    private fun updateInnerFragments() {
        pages.clear()
        pages.add(MainPagerAdapter.PagerModel(CurrencyFragment.newInstance(false), "Популярное"))
        pages.add(MainPagerAdapter.PagerModel(CurrencyFragment.newInstance(true), "Избранное"))
        pagesLive.value = pages
    }


}