package com.example.currencyexchange.repository

import androidx.lifecycle.MutableLiveData
import com.example.currencyexchange.model.CurrencyResponse
import com.example.currencyexchange.network.ApiService
import com.example.currencyexchange.utils.ApiException
import com.example.currencyexchange.utils.CurrencyConstants
import org.json.JSONException
import org.json.JSONObject
import java.lang.StringBuilder

class CurrencyRepository(private val apiService : ApiService) {

    val loading: MutableLiveData<Boolean> = MutableLiveData()

    val currencies: MutableLiveData<CurrencyResponse> = MutableLiveData()

    val currencyCountries : MutableLiveData<Set<String>> = MutableLiveData()

    suspend fun getCurrencyCountries() {

        loading.postValue(true)

        val response = apiService.fetchCurrency(CurrencyConstants.API_KEY)
        if(response.isSuccessful) {
            if(response.body() != null) {
                val currency  = response.body()
                currencyCountries.postValue(currency?.rates?.keys)
            }
        } else {
            val error = response.errorBody()?.string()
            val message = StringBuilder()
            println(error)
            error.let {
                try{
                    message.append(JSONObject(it).getString("message"))
                } catch (e : JSONException) { e.printStackTrace() }
            }
            message.append("\n")
            throw ApiException(message = message.toString())
        }

        loading.postValue(false)
    }

    suspend fun getCurrency() {

        loading.postValue(true)

        val response = apiService.fetchCurrency(CurrencyConstants.API_KEY)
        if(response.isSuccessful) {
            if(response.body() != null) {
                val currency  = response.body()
                currencies.postValue(currency)
            }
        } else {
            val error = response.errorBody()?.string()
            val message = StringBuilder()
            println(error)
            error.let {
                try{
                     message.append(JSONObject(it).getString("message"))
                } catch (e : JSONException) { e.printStackTrace() }
            }
            message.append("\n")
            throw ApiException(message = message.toString())
        }

        loading.postValue(false)
    }
}