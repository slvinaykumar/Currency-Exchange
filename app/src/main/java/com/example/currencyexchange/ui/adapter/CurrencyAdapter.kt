package com.example.currencyexchange.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyexchange.R
import com.example.currencyexchange.model.CurrencyResponse

class CurrencyAdapter(currencies : CurrencyResponse, currencyInput : Double) : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    var mCurrencies = currencies
    private lateinit var layout : View
    var mUserInput = currencyInput

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, null)

        return CurrencyViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return mCurrencies.rates.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val key = mCurrencies.rates.keys.toTypedArray()[position]
        val value = mCurrencies.rates[key]

        if(!key.isNullOrEmpty() && !key.isNullOrBlank() && value != null) {
            val currencyViewHolder = holder as CurrencyViewHolder
            currencyViewHolder.countryCode.text = key
            currencyViewHolder.currencyValue.text = calculateCurrency(value)
        }

    }

    private fun calculateCurrency(currency : Double) : String {

        val result = currency * mUserInput

        return result.toString()
    }

    class CurrencyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        var countryCode : TextView = itemView.findViewById(R.id.country_code)
        var currencyValue : TextView = itemView.findViewById(R.id.currency_value)
    }
}