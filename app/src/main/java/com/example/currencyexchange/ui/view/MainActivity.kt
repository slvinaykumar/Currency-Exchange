package com.example.currencyexchange.ui.view

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyexchange.R
import com.example.currencyexchange.model.CurrencyResponse
import com.example.currencyexchange.ui.adapter.CurrencyAdapter
import com.example.currencyexchange.ui.viewmodel.CurrencyViewModel
import com.example.currencyexchange.ui.viewmodel.CurrencyViewModelFactory
import com.example.currencyexchange.utils.TextChangedListener
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity(), KodeinAware, AdapterView.OnItemSelectedListener {


    override val kodein by kodein()
    private val factory: CurrencyViewModelFactory by instance()
    private lateinit var viewModel: CurrencyViewModel
    var userInput: Double = 0.0
    var currencyAdapter: CurrencyAdapter? = null
    val currencyCountrieList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this, factory).get(CurrencyViewModel::class.java)

        initUI()

        viewModel.getLoadingState().observe(this, Observer {
            showProgressBar(it)
        })

        viewModel.getCurrencyCountries().observe(this, Observer {
            setSpinner(it)

        })

        viewModel.getCurrency().observe(this, Observer {
            initRecyclerview(it)

        })

    }

    private fun setSpinner(currencyCountries: Set<String>) {

        currencyCountrieList.addAll(currencyCountries)

        val spinnerAdapter = ArrayAdapter<String>(
            this@MainActivity, // Context
            android.R.layout.simple_spinner_item, // Layout
            currencyCountrieList // Array
        )

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        if(viewModel.spinnerPosition.value != null) {
            spinner.setSelection(viewModel.spinnerPosition.value!!)
        }


        spinner.onItemSelectedListener = this
    }

    private fun initRecyclerview(currencies: CurrencyResponse) {

        if (currencyAdapter == null) {
            userInput = viewModel.userInput.value!!
            currencyAdapter = CurrencyAdapter(currencies, userInput)
            currency_recycler_view.apply {
                layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
                adapter = currencyAdapter
            }
        } else {
            currencyAdapter?.mCurrencies = currencies
            currencyAdapter?.notifyDataSetChanged()
        }
    }

    private fun initUI() {

        viewModel.searchCurrencyCountries()


        search_term.addTextChangedListener(object : TextChangedListener<EditText>(search_term) {
            override fun onTextChanged(target: EditText, s: Editable?) {
                if (!s.isNullOrBlank() && !s.isNullOrEmpty()) {
                    try {
                        userInput = s.toString().toDouble()
                        viewModel.userInput.postValue(userInput)
                    } catch (e: NumberFormatException) {
                        e.printStackTrace()
                    }

                }
            }

        })

        search_button.setOnClickListener {

            if (viewModel.userInput.value != null) {
                viewModel.searchCurrency()
                hideKeyBoard()
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        viewModel.spinnerPosition.postValue(position)
        spinner.setSelection(position)
    }

    private fun hideKeyBoard() {
        val imm: InputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    private fun showProgressBar(show: Boolean) {

        if (show) {
            progress_bar.visibility = View.VISIBLE
        } else {
            progress_bar.visibility = View.GONE
        }

    }


}