package com.test.application.ui.activities.main

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.test.application.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {


    private val mainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    private lateinit var binding: ActivityMainBinding

    private var mainPagerAdapter: MainPagerAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val adapter = FilteredDropDownAdapter(mainViewModel.CURRENCY_LIST)
        binding.spinnerItems.setAdapter(adapter)
        binding.spinnerItems.setText(mainViewModel.selectedCurrency, true)

        binding.spinnerOrder.hint = "Валюта"
        binding.spinnerItems.setOnItemClickListener { adapterView, view, i, l ->
            mainViewModel.updateBaseCurrency(mainViewModel.CURRENCY_LIST[i])
        }

        binding.imageSort.setOnClickListener {
            val popupMenu = androidx.appcompat.widget.PopupMenu(this, it)
            for (i in 0 until mainViewModel.sortArray.size) {
                val item = mainViewModel.sortArray[i]
                val itemName = SpannableString(item.second)
                val itemColor = if (item.first == mainViewModel.selectedCurrencySort) {
                    Color.BLUE
                } else {
                    Color.BLACK
                }

                itemName.setSpan(ForegroundColorSpan(itemColor), 0, itemName.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                popupMenu.menu.add(i, i, i, itemName)
            }

            popupMenu.setOnMenuItemClickListener {
                mainViewModel.updateCurrencySort(mainViewModel.sortArray[it.itemId].first)
                return@setOnMenuItemClickListener true
            }

            popupMenu.show()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        mainViewModel.pagesLive.observe(this, Observer {
            if (!isDestroyed) {
                mainPagerAdapter = MainPagerAdapter(this, it)
                binding.pager.adapter = mainPagerAdapter
                TabLayoutMediator(binding.tab, binding.pager) { tab, position ->
                    tab.text = mainViewModel.pages[position].title
                }.attach()
            }
        })
    }

}