package com.rapidops.salesmatechatsdk.app.extension

import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.rapidops.salesmatechatsdk.app.utils.ColorGenerator
import com.rapidops.salesmatechatsdk.app.view.TextDrawable
import com.rapidops.salesmatechatsdk.app.view.TextDrawable.Companion.builder


fun ImageView.loadImage(url: String?) {
    Glide.with(context)
        .applyDefaultRequestOptions(
            RequestOptions
                .diskCacheStrategyOf(DiskCacheStrategy.DATA)
        )
        .load(url).into(this)
}


fun ImageView.loadCircleProfileImage(url: String?, name: String? = "") {
    if (url.isNullOrEmpty()) {
        setImageDrawable(getTextDrawableFromName(name, layoutParams.height))
    } else {
        Glide.with(context)
            .applyDefaultRequestOptions(
                RequestOptions
                    .diskCacheStrategyOf(DiskCacheStrategy.DATA)
                    .circleCrop()
            )
            .load(url).into(this)
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


fun ImageView.loadPattern(messengerBackground: String) {
    setImageDrawable(
        ContextCompat.getDrawable(
            context,
            resources.getIdentifier(
                messengerBackground.replace("-", "_"),
                "drawable",
                context.packageName
            )
        )
    )
}