package com.example.myplaylistmaker.media.ui.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavoritesViewModel(text:String): ViewModel() {
    private val liveData = MutableLiveData(text)
}