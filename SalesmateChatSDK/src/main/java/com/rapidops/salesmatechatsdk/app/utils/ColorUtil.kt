package com.rapidops.salesmatechatsdk.app.utils

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.DrawableCompat
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
        val wrappedDrawable = DrawableCompat.wrap(background)
        DrawableCompat.setTint(wrappedDrawable, actionColor)
    }

    fun Drawable.setTintFromBackground() {
        DrawableCompat.setTint(this, backGroundColor.foregroundColor())
    }

    fun Drawable.getDrawableForBackground(): Drawable? {
        val wrappedDrawable = DrawableCompat.wrap(this)
        DrawableCompat.setTint(wrappedDrawable, backGroundColor.foregroundColor())
        return wrappedDrawable
    }

}