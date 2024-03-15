package com.example.myplaylistmaker.presentation

import android.content.Context
import android.widget.ImageView

interface GlideLoader {
    fun loadRoundedImage(context: Context, url: String, imageView: ImageView, radius: Int)
}