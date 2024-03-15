package com.example.myplaylistmaker.data

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.myplaylistmaker.R

class GlideLoaderImpl : GlideLoader {
    override fun loadRoundedImage(context: Context, url: String, imageView: ImageView, radius: Int) {
        Glide.with(context)
            .load(url)
            .transform(RoundedCorners(radius))
            .placeholder(R.drawable.placeholder)
            .into(imageView)
    }
}