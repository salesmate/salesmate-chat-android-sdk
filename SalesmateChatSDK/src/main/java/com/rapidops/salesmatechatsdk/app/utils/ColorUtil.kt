package com.rapidops.salesmatechatsdk.app.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.rapidops.salesmatechatsdk.R
import com.rapidops.salesmatechatsdk.core.SalesmateChat
import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.models.LookAndFeel

internal object ColorUtil {

    private var appSettingsDataSource: IAppSettingsDataSource =
        SalesmateChat.daggerDataComponent.getAppSettingsDataSource()

    private const val defaultBackgroundColor = "#1F62FF"
    private const val defaultActionColor = "#1F62FF"

    private val lookAndFeel: LookAndFeel by lazy {
        appSettingsDataSource.pingRes.lookAndFeel
    }


    val backGroundColor: Int by lazy {
        lookAndFeel.backgroundColor.takeIf { it.isNotEmpty() }?.let {
            Color.parseColor(it)
        } ?: run {
            Color.parseColor(defaultBackgroundColor)
        }
    }

    val actionColor: Int by lazy {
        lookAndFeel.actionColor.takeIf { it.isNotEmpty() }?.let {
            Color.parseColor(it)
        } ?: run {
            Color.parseColor(defaultActionColor)
        }
    }

    private fun Int.isDark(): Boolean {
        val darkness =
            1 - (0.299 * Color.red(this) + 0.587 * Color.green(this) + 0.114 * Color.blue(this)) / 255
        return darkness >= 0.5
    }


    private const val darkForegroundColor: String = "#000000"
    private const val secondaryDarkForegroundColor: String = "#47484A"

    private const val lightForegroundColor: String = "#FFFFFF"
    private const val secondaryLightForegroundColor: String = "#DEEBFF"


    /*val foregroundColor: Int by lazy {
        if (backGroundColor.isDark()) {
            Color.parseColor(lightForegroundColor)
        } else {
            Color.parseColor(darkForegroundColor)
        }
    }

    val foregroundActionColor: Int by lazy {
        if (actionColor.isDark()) {
            Color.parseColor(lightForegroundColor)
        } else {
            Color.parseColor(darkForegroundColor)
        }
    }

    val secondaryForegroundColor: Int by lazy {
        if (backGroundColor.isDark()) {
            Color.parseColor(secondaryLightForegroundColor)
        } else {
            Color.parseColor(secondaryDarkForegroundColor)
        }
    }

    val secondaryForegroundActionColor: Int by lazy {
        if (backGroundColor.isDark()) {
            Color.parseColor(secondaryLightForegroundColor)
        } else {
            Color.parseColor(secondaryDarkForegroundColor)
        }
    }*/

    fun Int.foregroundColor(): Int {
        return if (isDark()) {
            Color.parseColor(lightForegroundColor)
        } else {
            Color.parseColor(darkForegroundColor)
        }
    }

    fun Int.secondaryForegroundColor(): Int {
        return if (isDark()) {
            ColorUtils.setAlphaComponent(Color.parseColor(lightForegroundColor), 180)
            /*Color.parseColor(secondaryLightForegroundColor)*/
        } else {
            ColorUtils.setAlphaComponent(Color.parseColor(darkForegroundColor), 180)
            /*Color.parseColor(secondaryDarkForegroundColor)*/
        }
    }

    fun View.updateActionTint() {
        backgroundTintList = ColorStateList.valueOf(actionColor)
    }

    fun Drawable.setTintFromBackground() {
        setTint(backGroundColor.foregroundColor())
    }

    fun Drawable.setTintBackground() {
        setTint(backGroundColor)
    }

    val messengerBackground: String by lazy {
        lookAndFeel.messengerBackground
    }

    fun TextView.setSendButtonColorStateList() {
        val states = arrayOf(
            intArrayOf(android.R.attr.state_enabled),
            intArrayOf(-android.R.attr.state_enabled),
        )

        val colors = intArrayOf(
            actionColor,
            ContextCompat.getColor(context, R.color.hint_color),
        )
        setTextColor(ColorStateList(states, colors))
    }

    fun FrameLayout.updateBackgroundPattern() {
        val layerDrawable = background as LayerDrawable
        val drawable = context.getPatternDrawableFromName(messengerBackground) as BitmapDrawable
        drawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        drawable.alpha = 20
        layerDrawable.setDrawableByLayerId(R.id.back_pattern, drawable)
        layerDrawable.findDrawableByLayerId(R.id.back_color).setTint(backGroundColor)
    }

    private fun Context.getPatternDrawableFromName(messengerBackground: String): Drawable? {
        var identifier = resources.getIdentifier(
            messengerBackground.replace("-", "_"),
            "drawable",
            packageName
        )
        if (identifier == 0) {
            identifier = R.drawable.pattern_1
        }
        return ContextCompat.getDrawable(this, identifier)
    }

}