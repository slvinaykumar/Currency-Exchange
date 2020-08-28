package com.example.currencyexchange.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.currencyexchange.repository.CurrencyRepository

class CurrencyViewModelFactory (private val currencyRepository: CurrencyRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CurrencyViewModel(
            currencyRepository
        ) as T
    }
}