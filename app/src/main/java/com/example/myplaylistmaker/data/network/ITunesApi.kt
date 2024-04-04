package com.example.myplaylistmaker.data.network

import com.example.myplaylistmaker.models.ITunesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<ITunesResponse>
}