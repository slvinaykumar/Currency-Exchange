package com.example.currencyexchange.network

import com.example.currencyexchange.model.CurrencyResponse
import com.example.currencyexchange.utils.CurrencyConstants
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {


    @GET("latest?")
    suspend fun fetchCurrency(@Query("access_key") apiKey : String) : Response<CurrencyResponse>

    //Used when we want to select the base currency and get results accordingly and it cannot be used with the current API KEY
    //if used with current API key we will ge the following JSON response
   /* {
        "success": false,
        "error": {
        "code": 105,
        "type": "base_currency_access_restricted"
    }
    }*/
    @GET("latest?")
    suspend fun fetchCurrencyWithbase(@Query("access_key") apiKey : String,
                                        @Query("base") base : String) : Response<CurrencyResponse>

    companion object{
        operator fun invoke(networkConnectionInterceptor: NetworkConnectionInterceptor) : ApiService{

            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl(CurrencyConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }

}