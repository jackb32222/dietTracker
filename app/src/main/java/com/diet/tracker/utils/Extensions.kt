package com.diet.tracker.utils

import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.getInt(defValue: Int = 0) : Int {
    return editText?.text?.toString()?.toIntOrNull() ?: defValue
}

fun TextInputLayout.getDouble(defValue: Double = 0.0) : Double {
    return editText?.text?.toString()?.toDoubleOrNull() ?: defValue
}

fun Long.parseToString() : String {
    val timeInSeconds = div(1000)
    val hours = timeInSeconds.div(3600)
    val remain = timeInSeconds.rem(3600)
    val minutes = remain.div(60)
    val seconds = remain.rem(60)

    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

fun Int.convertToString() = if (this <= 0) "" else toString()