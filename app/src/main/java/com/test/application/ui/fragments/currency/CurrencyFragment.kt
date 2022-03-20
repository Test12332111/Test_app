package com.test.application.ui.fragments.currency

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.test.application.R
import com.test.application.data.ui.models.CurrencyUI
import com.test.application.databinding.FragmentCurrencyBinding


class CurrencyFragment : Fragment(), CurrencyAdapter.OnCurrencyListener {

    private val currencyViewModel by lazy {
        ViewModelProvider(
            this, CurrencyViewModelFactory(
                requireArguments().getBoolean(BUNDLE_IS_FAVORITE)
            )
        ).get(CurrencyViewModel::class.java)
    }


    private var binding: FragmentCurrencyBinding? = null
    private val requireBinding: FragmentCurrencyBinding
        get() = binding!!

    private val currencyAdapter by lazy {
        CurrencyAdapter()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_currency, container, false)
        binding = FragmentCurrencyBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initItemsRecycleList()
        requireBinding.swipeItems.setOnRefreshListener {
            currencyViewModel.refresh()
        }

        observeViewModel()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onHandleFavorite(favorite: Boolean, currencyUI: CurrencyUI) {
        currencyViewModel.handleFavorite(favorite, currencyUI)
    }

    private fun observeViewModel() {
        currencyViewModel.onToast.observe(this, Observer {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })

        currencyViewModel.currencyListLive.observe(this, Observer {
            currencyAdapter.update(it)
        })

        currencyViewModel.onRefresh.observe(this, Observer {
            requireBinding.swipeItems.isRefreshing = it
        })

        currencyViewModel.onPlaceHolderChangeState.observe(this, Observer {
            if (it) {
                requireBinding.placeholder.visibility = View.VISIBLE
                val error = currencyViewModel.placeHolderError
                if (error != null) {
                    requireBinding.placeholder.lottieRes = R.raw.lottie_error
                    requireBinding.placeholder.text = error
                    requireBinding.btnErrorAction.visibility = View.VISIBLE
                    requireBinding.btnErrorAction.text = "Повторить"
                    requireBinding.btnErrorAction.setOnClickListener {
                        currencyViewModel.refresh()
                    }

                } else {
                    requireBinding.placeholder.lottieRes = R.raw.lottie_empty_list
                    requireBinding.placeholder.text = if (currencyViewModel.favoritePage) {
                        "Пока ничего не добавлено в раздел избранное"
                    } else {
                        "Список курсов с выбранной валютой пуст. Побробуйте выбрать другую."
                    }
                    requireBinding.btnErrorAction.visibility = View.GONE

                }
            } else {
                requireBinding.placeholder.visibility = View.GONE
            }


        })
    }


    private fun initItemsRecycleList() {
        val linearManager = LinearLayoutManager(activity)
        val defaultAnimationAdapter = DefaultItemAnimator()
        this.requireBinding.recyclerItems.itemAnimator = defaultAnimationAdapter
        this.requireBinding.recyclerItems.layoutManager = linearManager
        this.requireBinding.recyclerItems.setHasFixedSize(true)
        this.currencyAdapter.onCurrencyListener = this
        this.requireBinding.recyclerItems.adapter = currencyAdapter
    }

    companion object {

        private const val BUNDLE_IS_FAVORITE = "BUNDLE_IS_FAVORITE"

        fun newInstance(favorite: Boolean): CurrencyFragment {
            val fragment = CurrencyFragment()
            val bundle = Bundle()
            bundle.putBoolean(BUNDLE_IS_FAVORITE, favorite)
            fragment.arguments = bundle
            return fragment
        }
    }

}