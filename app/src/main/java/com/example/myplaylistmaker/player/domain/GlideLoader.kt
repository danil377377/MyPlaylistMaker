package com.example.myplaylistmaker.player.domain

import android.content.Context
import android.widget.ImageView

interface GlideLoader {
    fun loadRoundedImage(context: Context, url: String, imageView: ImageView)
}