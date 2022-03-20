package com.test.application.eventbus.events



class SortCurrencyEvent(val sort: String) {

    companion object {
        const val SORT_NAME_ASC = "SORT_NAME_ASC"
        const val SORT_NAME_DESC = "SORT_NAME_DESC"
        const val SORT_VALUE_ASC = "SORT_VALUE_ASC"
        const val SORT_VALUE_DESC = "SORT_VALUE_DESC"
    }

}
