package com.example.currencyexchange.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyexchange.repository.CurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrencyViewModel(private val currencyRepository: CurrencyRepository) : ViewModel() {

    val userInput : MutableLiveData<Double> = MutableLiveData()

    val spinnerPosition : MutableLiveData<Int> = MutableLiveData()

    fun getLoadingState() = currencyRepository.loading

    fun getCurrency() = currencyRepository.currencies

    fun getCurrencyCountries() = currencyRepository.currencyCountries


    //first Api call to get the countries to show on drop down list
    fun searchCurrencyCountries() {
        viewModelScope.launch(Dispatchers.IO) {
            currencyRepository.getCurrencyCountries()
        }
    }

    //second api call to get the response
    fun searchCurrency()  {
        viewModelScope.launch(Dispatchers.IO) {
            currencyRepository.getCurrency()
        }
    }

}