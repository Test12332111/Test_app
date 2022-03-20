package com.test.application.ui.fragments.currency

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.application.App
import com.test.application.data.CurrencyService
import com.test.application.data.local.GlobalSettings
import com.test.application.data.ui.models.CurrencyUI
import com.test.application.eventbus.EventBus
import com.test.application.eventbus.events.SortCurrencyEvent

import com.test.application.livedata.SingleLiveEvent
import com.test.application.utils.Utils
import kotlinx.coroutines.*
import java.lang.IllegalArgumentException

import javax.inject.Inject


class CurrencyViewModel(val favoritePage: Boolean) : ViewModel() {


    @Inject
    lateinit var currencyService: CurrencyService


    @Inject
    lateinit var eventBus: EventBus

    @Inject
    lateinit var globalSettings: GlobalSettings

    @Inject
    lateinit var application: Application

    val currencyListLive: MutableLiveData<List<CurrencyUI>> = MutableLiveData()
    val onRefresh: MutableLiveData<Boolean> = MutableLiveData()
    val onPlaceHolderChangeState: MutableLiveData<Boolean> = MutableLiveData()
    val onToast: SingleLiveEvent<String> = SingleLiveEvent()

    private val currencyList = ArrayList<CurrencyUI>()

    private var getCurrencyJob: Job? = null
    private var favoriteCurrencyJob: Job? = null
    private var subscriptionBaseCurrencyJob: Job? = null
    private var subscriptionCurrencySortJob: Job? = null
    private var subscriptionFavoriteJob: Job? = null


    var placeHolderError: String? = null
        private set

    private var baseCurrency: String? = null
    private var sortType: String? = null


    init {
        App.getApplicationComponent().inject(this)
        subscribeCurrencySort()
        subscribeOnBaseCurrency()
        subscriptionFavorite()
    }

    override fun onCleared() {
        super.onCleared()
        getCurrencyJob?.cancel()
        favoriteCurrencyJob?.cancel()
        subscriptionBaseCurrencyJob?.cancel()
        subscriptionCurrencySortJob?.cancel()
        subscriptionFavoriteJob?.cancel()
    }

    fun refresh() {
        baseCurrency.let {
            if (it != null) {
                updateCurrencyList(it)
            } else {
                onRefresh.value = false
                onToast.value = "Основная валюта не выбрана. Выберите основную валюту"
            }
        }
    }

    private fun updateCurrencyList(base: String, forceUpdate: Boolean = false) {
        if (forceUpdate) {
            currencyList.clear()
            sortAndUpdateItems()
        }

        placeHolderError = null
        onRefresh.value = true
        onPlaceHolderChangeState.value = false
        getCurrencyJob?.cancel()
        getCurrencyJob =
            CoroutineScope(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
                GlobalScope.launch(Dispatchers.Main) {
                    onRefresh.value = false
                    val errorText = Utils.buildHttpErr(throwable, "Что-то пошло не так.")
                    if (currencyList.isEmpty()) {
                        placeHolderError = errorText
                        onPlaceHolderChangeState.value = true
                    } else {
                        onToast.value = errorText
                    }
                }

            }).launch {
                val list = if (favoritePage) {
                    currencyService.getFavoriteList(base)
                } else {
                    currencyService.getCurrencyList(base)
                }

                GlobalScope.launch(Dispatchers.Main) {
                    placeHolderError = null
                    onRefresh.value = false
                    currencyList.clear()
                    currencyList.addAll(list)
                    sortAndUpdateItems()
                }
            }
    }

    fun handleFavorite(favorite: Boolean, currency: CurrencyUI) {
        favoriteCurrencyJob =
            CoroutineScope(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
                GlobalScope.launch(Dispatchers.Main) {
                    onToast.value = if (favorite) {
                        "Ошибка добавления в избранное"
                    } else {
                        "Ошибка удаления из избранного"
                    }
                }

            }).launch {
                currencyService.handleFavorite(favorite, currency)
            }
    }

    private fun sortAndUpdateItems() {
        when (sortType) {
            SortCurrencyEvent.SORT_NAME_ASC -> {
                currencyList.sortBy { it.secondCurrency }
            }

            SortCurrencyEvent.SORT_NAME_DESC -> {
                currencyList.sortByDescending { it.secondCurrency }
            }

            SortCurrencyEvent.SORT_VALUE_ASC -> {
                currencyList.sortBy { it.rate }
            }

            SortCurrencyEvent.SORT_VALUE_DESC -> {
                currencyList.sortByDescending { it.rate }
            }

            null -> {
                //skip
            }

            else -> {
                throw IllegalArgumentException("Wrong sort type ${sortType}")
            }

        }

        onPlaceHolderChangeState.value = currencyList.isEmpty()
        currencyListLive.value = currencyList
    }

    private fun subscribeOnBaseCurrency() {
        subscriptionBaseCurrencyJob?.cancel()
        subscriptionBaseCurrencyJob = GlobalScope.launch {
            eventBus.updateBaseCurrency
                .collect {
                    withContext(Dispatchers.Main) {
                        globalSettings.currentCurrency?.let {
                            if (baseCurrency != it) {
                                baseCurrency = it
                                updateCurrencyList(it, true)
                            }
                        }
                    }
                }
        }
    }

    private fun subscribeCurrencySort() {
        subscriptionCurrencySortJob?.cancel()
        subscriptionCurrencySortJob = GlobalScope.launch {
            eventBus.sortCurrency
                .collect {
                    withContext(Dispatchers.Main) {
                        if (sortType != it.sort) {
                            sortType = it.sort
                            if (currencyList.isNotEmpty()) {
                                sortAndUpdateItems()
                            }
                        }
                    }
                }
        }
    }

    private fun subscriptionFavorite() {
        subscriptionFavoriteJob?.cancel()
        subscriptionFavoriteJob = GlobalScope.launch {
            currencyService.handleFavoriteChannel
                .collect { currency ->
                    withContext(Dispatchers.Main) {
                        if (baseCurrency == currency.base) {
                            val currentItem = currencyList.firstOrNull { it.id == currency.id }
                            if (favoritePage) {
                                if (currentItem == null) {
                                    if (currency.isFavorite) {
                                        currencyList.add(currency)
                                        sortAndUpdateItems()
                                    }
                                } else {
                                    if (currency.isFavorite.not()) {
                                        currencyList.removeAll { it.id == currency.id }
                                        sortAndUpdateItems()
                                    }
                                }
                            } else {
                                if (currentItem != null) {
                                    currencyList[currencyList.indexOf(currentItem)] = currency
                                    sortAndUpdateItems()
                                }
                            }
                        }
                    }
                }
        }
    }
}
