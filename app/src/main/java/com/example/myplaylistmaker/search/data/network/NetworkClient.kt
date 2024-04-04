package com.example.myplaylistmaker.search.data.network

import com.example.myplaylistmaker.search.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response

}