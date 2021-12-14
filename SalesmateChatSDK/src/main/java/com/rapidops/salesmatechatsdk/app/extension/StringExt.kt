package com.rapidops.salesmatechatsdk.app.extension

import android.text.Html
import android.text.Spanned
import android.util.Base64
import com.rapidops.salesmatechatsdk.R
import java.util.regex.Matcher
import java.util.regex.Pattern


internal fun String.fromNormalHtml(): Spanned {
    val result: Spanned =
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(this)
        }
    return result
}


internal fun String.getExtensionFromFile(): String {
    return this.substringAfterLast(".").lowercase().trim()
}

internal fun String.getResourceIdFromFileExtension(): Int {
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


fun String.isValidEmail(): Boolean = if (this.isEmpty()) {
    false
} else {
    val emailPattern =
        "^[A-Za-z\\d._\\-+]{3,64}@([A-Za-z\\d]+)\\.[A-Za-z\\d]+(.[A-Za-z\\d]+)?$"
    val pattern = Pattern.compile(emailPattern)
    val matcher: Matcher = pattern.matcher(this)
    matcher.matches()
}


internal fun String.encrypt(): String {
    return Base64.encodeToString(this.toByteArray(), Base64.DEFAULT).trim { it <= ' ' }
}

internal fun String.decrypt(): String {
    return String(Base64.decode(this, Base64.DEFAULT)).trim { it <= ' ' }
}

internal fun String.getEmojiByUnicode(): String {
    val code = this.toInt(16)
    return String(Character.toChars(code))
}

internal fun String.getEmoji(): String {
    return if (this == "2639") {
        "☹️"
    } else {
        "&#x${this}"
    }
}