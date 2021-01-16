@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.uas_0127.utils

import android.view.View
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}
private fun formatDate(date: String, format: String): String {
    var result = ""
    val old = SimpleDateFormat("yyyy-MM-dd")

    try {
        val oldDate = old.parse(date)
        val newFormat = SimpleDateFormat(format)

        result = newFormat.format(oldDate)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return result
}

fun getLongDate(date: String?): String {
    return formatDate(date.toString(), "yyyy MMM dd")
}

fun String.parseLineup(delimiter: String = "; ", replacement: String = ";\n" ): String {
    return this.replace(delimiter, replacement)
}

fun String.parseDetail(delimiter: String = ";", replacement: String = ";\n"): String {
    return this.replace(delimiter, replacement)
}

fun String.formatDate(oldFormat: String="dd/MM/yy", newFormat: String = "yyyy MMM dd"): String {
    val date = SimpleDateFormat(oldFormat, Locale.US).parse(this)
    val format = SimpleDateFormat(newFormat, Locale.US)
    return format.format(date)
}

fun String.formatTime(oldFormat: String="HH:mm:ss", newFormat: String = "hh:mm aa"): String {
    val time = SimpleDateFormat(oldFormat, Locale.US).parse(this)
    val format = SimpleDateFormat(newFormat, Locale.US)
    format.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
    return format.format(time)
}