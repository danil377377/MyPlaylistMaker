package com.example.myplaylistmaker.search.data.network

import com.example.myplaylistmaker.search.data.dto.ITunesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<ITunesResponse>
}