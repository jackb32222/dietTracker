package com.diet.tracker.utils

import android.view.View
import android.widget.TextView
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun View.isVisible(keepBounds: Boolean): ReadWriteProperty<Any, Boolean> =
    object : ReadWriteProperty<Any, Boolean> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Boolean =
            visibility == View.VISIBLE

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
            visibility = when {
                value -> View.VISIBLE
                keepBounds -> View.INVISIBLE
                else -> View.GONE
            }
        }
    }

fun View.isEnable(): ReadWriteProperty<Any, Boolean> =
    object : ReadWriteProperty<Any, Boolean> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Boolean =
            isEnabled

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
            isEnabled = value
        }
    }

fun TextView?.text(): ReadWriteProperty<Any, String> {
    val textView = this
    return object : ReadWriteProperty<Any, String> {
        override fun getValue(thisRef: Any, property: KProperty<*>): String {
            return textView?.text?.toString().orEmpty()
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: String) {
            textView?.text = value
        }
    }
}