package com.test.application.eventbus


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.test.application.eventbus.events.SortCurrencyEvent
import com.test.application.eventbus.events.UpdateBaseCurrencyEvent


class EventBus {

    val sortCurrency = MutableSharedFlow<SortCurrencyEvent>(onBufferOverflow = BufferOverflow.SUSPEND, replay = 1, extraBufferCapacity = 1)
    val updateBaseCurrency = MutableSharedFlow<UpdateBaseCurrencyEvent>(onBufferOverflow = BufferOverflow.SUSPEND, replay = 1, extraBufferCapacity = 1)


    private val broadcastChannel = MutableSharedFlow<Any>(onBufferOverflow = BufferOverflow.SUSPEND)


    fun post(event: Any) {
         GlobalScope.launch(Dispatchers.Main) {
             broadcastChannel.emit(event)
         }
    }

    fun observable(): SharedFlow<Any> {
        return broadcastChannel.asSharedFlow()
    }

    fun <T> filteredObservable(eventClass: Class<T>): Flow<T> {
        return broadcastChannel.asSharedFlow()
            .filter { it.javaClass == eventClass }
            .map { it as T }
    }
}