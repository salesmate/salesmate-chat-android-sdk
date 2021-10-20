package com.rapidops.salesmatechatsdk.app.extension

import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.rapidops.salesmatechatsdk.R
import com.rapidops.salesmatechatsdk.app.utils.ColorGenerator
import com.rapidops.salesmatechatsdk.app.utils.transformations.CircleTransform
import com.rapidops.salesmatechatsdk.app.utils.transformations.RoundedTransformation
import com.rapidops.salesmatechatsdk.app.view.TextDrawable
import com.rapidops.salesmatechatsdk.app.view.TextDrawable.Companion.builder
import com.squareup.picasso.Picasso


fun ImageView.loadImage(url: String?) {
    /*Glide.with(context)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .into(this)*/
    Picasso.get().load(url).into(this)

}

fun AppCompatImageView.loadImageWithRoundedTransformation(url: String?) {
    val dimension = context.resources.getDimension(R.dimen.img_rounded_corner)
    /*Glide.with(context)
        .load(url)
        .into(this)*/

    Picasso.get().load(url).transform(RoundedTransformation(dimension)).into(this)
}


fun AppCompatImageView.loadCircleProfileImage(url: String?, name: String? = "") {
    if (url.isNullOrEmpty()) {
        setImageDrawable(getTextDrawableFromName(name, layoutParams.height))
    } else {
        Picasso.get().load(url).transform(CircleTransform()).into(this)
        /*Glide.with(context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .circleCrop().into(this)*/
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