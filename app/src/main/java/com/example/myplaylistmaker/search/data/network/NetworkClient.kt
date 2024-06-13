package com.example.myplaylistmaker.search.data.network

import com.example.myplaylistmaker.search.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response

}