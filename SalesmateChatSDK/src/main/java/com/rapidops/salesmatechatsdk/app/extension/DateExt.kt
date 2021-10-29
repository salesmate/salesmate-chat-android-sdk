package com.rapidops.salesmatechatsdk.app.extension


import android.text.format.DateUtils
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit


internal enum class DateFormatType(val value: String) {
    yyyy_MM_dd_T_HH_mm_ss_SSS_Z("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
    MMM_dd_yyyy_hh_mm_a("MMM dd, yyyy, hh:mm a"),
    MMM_dd_hh_mm_a("MMM dd, hh:mm a"),
    hh_mm_a("hh:mm a");
}

internal fun DateTime?.getMessageDate(): CharSequence? {
    return DateTimeFormat.mediumDate().print(this)
}

internal fun DateTime?.isToday(): Boolean {
    if (this == null) {
        return false
    } else {
        return DateUtils.isToday(this.millis)
    }
}

internal fun String.parseFromISOFormat(): DateTime {
    return DateTime.parse(
        this,
        DateTimeFormat.forPattern(DateFormatType.yyyy_MM_dd_T_HH_mm_ss_SSS_Z.value).withZoneUTC()
    ).withZone(DateTimeZone.getDefault())
}

internal fun String.parseDate(
    currentFormatType: DateFormatType,
    outputFormatType: DateFormatType
): String {
    val parse = DateTime.parse(this, DateTimeFormat.forPattern(currentFormatType.value))
    val formatter = DateTimeFormat.forPattern(outputFormatType.value)
    return formatter.print(parse)
}

internal fun String.parseDate(currentFormatType: DateFormatType): DateTime {
    val parse = DateTime.parse(this, DateTimeFormat.forPattern(currentFormatType.value))
    return parse
}

internal fun String.parseDate(currentFormatType: DateTimeFormatter): DateTime {
    val parse = DateTime.parse(this, currentFormatType)
    return parse
}


internal fun DateTime.parseDate(outputFormatType: DateFormatType): String {
    val formatter = DateTimeFormat.forPattern(outputFormatType.value)
    return formatter.print(this)
}

internal fun DateTime.convertDefaultToUTC(): DateTime {
    return this.withZone(DateTimeZone.UTC)
}

internal fun DateTime.convertUTCToDefault(): DateTime {
    return this.withZone(DateTimeZone.getDefault())
}

internal fun Date.convertDefaultToUTC(): Date {
    val dateTime = DateTime(this, DateTimeZone.getDefault())
    val withZone = dateTime.withZone(DateTimeZone.UTC).toLocalDateTime()
    return withZone.toDate()
}

internal fun Date.convertUTCToDefault(): Date {
    val dateTime = DateTime(this, DateTimeZone.UTC)
    val withZone = dateTime.withZone(DateTimeZone.getDefault()).toLocalDateTime()
    return withZone.toDate()
}

internal fun String.getRelativeTimeStringFromPostTime(): String {
    val parse =
        DateTime.parse(
            this,
            DateTimeFormat.forPattern(DateFormatType.yyyy_MM_dd_T_HH_mm_ss_SSS_Z.value)
                .withZoneUTC()
        )
    return DateUtils.getRelativeTimeSpanString(parse.millis).toString()
}


internal fun String.getPeriod(): String {
    return parseFromISOFormat().getPeriod()
}

internal fun DateTime.getPeriod(): String {
    val difference = DateTime.now().minus(millis).millis
    val toDays = TimeUnit.MILLISECONDS.toDays(difference)
    val toHours = TimeUnit.MILLISECONDS.toHours(difference)
    val toMinutes = TimeUnit.MILLISECONDS.toMinutes(difference)
    return when {
        toDays != 0L -> {
            toDays.toString() + "d"
        }
        toHours != 0L -> {
            toHours.toString() + "h"
        }
        toMinutes != 0L -> {
            toMinutes.toString() + "m"
        }
        else -> {
            "Just now"
        }
    }
}

internal fun String.getMessageTime(): String {
    val dateTime = parseFromISOFormat()
    val now = DateTime.now()
    val difference = now.minus(dateTime.millis).millis
    val toDays = TimeUnit.MILLISECONDS.toDays(difference)
    val toHours = TimeUnit.MILLISECONDS.toHours(difference)
    val toMinutes = TimeUnit.MILLISECONDS.toMinutes(difference)
    return when {
        toDays != 0L -> {
            if (dateTime.year == now.year) {
                dateTime.parseDate(DateFormatType.MMM_dd_hh_mm_a)
            } else {
                dateTime.parseDate(DateFormatType.MMM_dd_yyyy_hh_mm_a)
            }
        }
        toHours != 0L -> {
            toHours.toString() + "h ago"
        }
        toMinutes != 0L -> {
            toMinutes.toString() + "m ago"
        }
        else -> {
            "Just now"
        }
    }

}

internal object DateUtil {

    fun getCurrentISOFormatDateTime(): String {
        return DateTime.now().getISOFormatDate()
    }

    private fun DateTime.getISOFormatDate(): String {
        return withZone(DateTimeZone.UTC).parseDate(DateFormatType.yyyy_MM_dd_T_HH_mm_ss_SSS_Z)
    }

    fun String.isCurrentWeekDay(): Boolean {
        return this.equals(DateTime.now().dayOfWeek().asText.lowercase(), true)
    }

    private val weekDay = arrayOf(1, 2, 3, 4, 5)
    private val weekEnds = arrayOf(6, 7)

    const val WEEK_DAYS = "weekdays"
    const val WEEK_ENDS = "weekends"

    fun getWeekDayTypeOfToday(): String {
        return if (weekEnds.contains(DateTime.now().dayOfWeek)) WEEK_ENDS else WEEK_DAYS
    }


    fun isTodayInBetween(startTimeStr: String, endTimeStr: String): Boolean {
        val startTime = startTimeStr.parseDate(DateFormatType.hh_mm_a)
        val endTime = endTimeStr.parseDate(DateFormatType.hh_mm_a)
        val currentDateTime = DateTime.now()
        val startDateTime = DateTime(
            currentDateTime.year,
            currentDateTime.monthOfYear,
            currentDateTime.dayOfMonth,
            startTime.hourOfDay,
            startTime.minuteOfHour
        )

        val endDateTime = DateTime(
            currentDateTime.year,
            currentDateTime.monthOfYear,
            currentDateTime.dayOfMonth,
            endTime.hourOfDay,
            endTime.minuteOfHour
        )
        return startDateTime.isBeforeNow && endDateTime.isAfterNow
    }
}