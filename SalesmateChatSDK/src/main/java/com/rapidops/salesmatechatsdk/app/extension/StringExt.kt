package com.rapidops.salesmatechatsdk.app.extension

import android.text.Html
import android.text.Spanned
import com.rapidops.salesmatechatsdk.R


fun String.fromNormalHtml(): Spanned {
    val result: Spanned = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }
    return result
}


fun String.getExtensionFromFile(): String {
    return this.substringAfterLast(".").lowercase().trim()
}

fun String.getResourceIdFromFileExtension(): Int {
    return when (getExtensionFromFile()) {
        "jpg", "jpeg", "png", "gif", "ico" -> {
            R.drawable.ic_attachment_jpeg
        }
        "doc", "docx" -> {
            R.drawable.ic_attachment_doc
        }
        "pdf" -> {
            R.drawable.ic_attachment_pdf
        }
        "txt" -> {
            R.drawable.ic_attachment_txt
        }
        "xls", "xlt", "xlm", "xlsx", "xlsm", "xltm", "xlmb", "xla", "xlam", "xll", "xlw", "csv" -> {
            R.drawable.ic_attachment_xls
        }
        "zip" -> {
            R.drawable.ic_attachment_zip
        }
        "avi", "flv", "mov", "mp4", "wmv" -> {
            R.drawable.ic_attachment_video
        }
        "m4a", "flac", "mp3", "wav", "wma", "aac" -> {
            R.drawable.ic_attachment_audio
        }
        else -> {
            R.drawable.ic_attachment_other
        }
    }
}