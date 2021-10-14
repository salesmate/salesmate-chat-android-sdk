package com.rapidops.salesmatechatsdk.app.extension

import android.text.Html
import android.text.Spanned


fun String.fromNormalHtml(): Spanned {
    val result: Spanned
    result = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }
    return result
}
