package com.example.myplaylistmaker.player.data

import android.content.Context
import android.util.TypedValue
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.player.domain.GlideLoader

class GlideLoaderImpl(context: Context) : GlideLoader {
    val radiusInDp = 8 // радиус скругления в dp
    val radiusInPixels = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        radiusInDp.toFloat(),
        context.resources.displayMetrics
    ).toInt()
    override fun loadRoundedImage(context: Context, url: String, imageView: ImageView) {
        Glide.with(context)
            .load(url)
            .transform(RoundedCorners(radiusInPixels))
            .placeholder(R.drawable.placeholder)
            .into(imageView)
    }
}