package com.rapidops.salesmatechatsdk.app.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.graphics.ColorUtils
import com.rapidops.salesmatechatsdk.R
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.foregroundColor
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.secondaryForegroundColor

/**
 * General text view object of AppCompatTextView.
 */
internal open class AppTextView : AppCompatTextView {

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.AppTextView)

        if (attr.hasValue(R.styleable.AppTextView_textColorStyle)) {
            val styleValue = attr.getInt(R.styleable.AppTextView_textColorStyle, 0)

            when (styleValue) {
                1 -> {
                    setTextColor(ColorUtil.backGroundColor.foregroundColor())
                }
                2 -> {
                    setTextColor(ColorUtil.backGroundColor.secondaryForegroundColor())
                }
                3 -> {
                    setTextColor(ColorUtil.actionColor.foregroundColor())
                }
                4 -> {
                    setTextColor(ColorUtil.actionColor.secondaryForegroundColor())
                }
            }
        }

        attr.recycle()
    }


}
