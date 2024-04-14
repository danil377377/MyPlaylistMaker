package com.example.myplaylistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.myplaylistmaker.search.data.dto.ITunesRequest
import com.example.myplaylistmaker.search.data.dto.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val context: Context, val ITunesService: ITunesApi) : NetworkClient {

//    private val ITunesBaseUrl = "https://itunes.apple.com"
//    private val retrofit = Retrofit.Builder()
//        .baseUrl(ITunesBaseUrl)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()



    override fun doRequest(dto: Any): Response {
        if (isConnected() == false) {
            return Response().apply { resultCode = -1 }
        }
        if (dto !is ITunesRequest) {
            return Response().apply { resultCode = 400 }
        }

        val response = ITunesService.search(dto.expression).execute()
        val body = response.body()
        return if (body != null) {
            body.apply { resultCode = response.code() }
        } else {
            Response().apply { resultCode = response.code() }
        }
    }
    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}