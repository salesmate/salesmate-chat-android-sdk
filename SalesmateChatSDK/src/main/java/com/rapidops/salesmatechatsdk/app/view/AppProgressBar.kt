package com.rapidops.salesmatechatsdk.app.view

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.widget.ProgressBar
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil

/**
 * General text view object of ProgressBar.
 */
internal open class AppProgressBar : ProgressBar {

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

        indeterminateTintList = ColorStateList.valueOf(ColorUtil.actionColor)

    }


}
