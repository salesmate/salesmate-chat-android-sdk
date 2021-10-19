package com.rapidops.salesmatechatsdk.app.extension

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.rapidops.salesmatechatsdk.R
import com.rapidops.salesmatechatsdk.app.utils.ColorGenerator
import com.rapidops.salesmatechatsdk.app.view.TextDrawable
import com.rapidops.salesmatechatsdk.app.view.TextDrawable.Companion.builder


fun ImageView.loadImage(url: String?) {
    Glide.with(context)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .into(this)
}

fun ImageView.loadImageWithRoundedTransformation(url: String?) {
    val dimension = context.resources.getDimension(R.dimen.img_rounded_corner)
    Glide.with(context)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .transform(RoundedCorners(dimension.toInt()))
        .into(this)
}


fun ImageView.loadCircleProfileImage(url: String?, name: String? = "") {
    if (url.isNullOrEmpty()) {
        setImageDrawable(getTextDrawableFromName(name, layoutParams.height))
    } else {
        Glide.with(context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .circleCrop().into(this)
    }

}

private fun getTextDrawableFromName(
    text: String?,
    imageSize: Int
): TextDrawable {
    val value = text?.takeIf { it.isNotEmpty() }?.first() ?: "A"
    val colorGenerator = ColorGenerator.MATERIAL4
    val color = colorGenerator.getColor(value)
    return builder().beginConfig()
        .fontSize((imageSize * 0.45).toInt())
        .endConfig()
        .buildRound(value.toString(), color)
}