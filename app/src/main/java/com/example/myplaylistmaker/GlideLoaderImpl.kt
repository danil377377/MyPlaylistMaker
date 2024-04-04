package com.example.myplaylistmaker

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.myplaylistmaker.presentation.GlideLoader

class GlideLoaderImpl : GlideLoader {
    override fun loadRoundedImage(context: Context, url: String, imageView: ImageView, radius: Int) {
        Glide.with(context)
            .load(url)
            .transform(RoundedCorners(radius))
            .placeholder(R.drawable.placeholder)
            .into(imageView)
    }
}