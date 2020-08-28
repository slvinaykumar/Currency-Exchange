package com.example.currencyexchange

import android.app.Application
import com.example.currencyexchange.network.ApiService
import com.example.currencyexchange.network.NetworkConnectionInterceptor
import com.example.currencyexchange.repository.CurrencyRepository
import com.example.currencyexchange.ui.viewmodel.CurrencyViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class CurrencyApplication : Application(), KodeinAware {
    override val kodein: Kodein = Kodein.lazy {

        import(androidXModule(this@CurrencyApplication))

        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { ApiService(instance()) }
        bind() from singleton { CurrencyViewModelFactory(instance()) }
        bind() from singleton { CurrencyRepository(instance()) }

    }
}