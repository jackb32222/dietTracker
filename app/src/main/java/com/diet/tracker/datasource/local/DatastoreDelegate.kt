package com.diet.tracker.datasource.local

import kotlinx.coroutines.flow.Flow
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun AppDataStore.string(
    key: String,
    defaultValue: String = ""
): ReadWriteProperty<Any, String> =
    object : ReadWriteProperty<Any, String> {
        override fun getValue(
            thisRef: Any,
            property: KProperty<*>
        ) = getString(key, defaultValue)

        override fun setValue(
            thisRef: Any,
            property: KProperty<*>,
            value: String
        ) = putString(key, value)
    }

fun AppDataStore.flowString(
    key: String,
    defaultValue: String = ""
): ReadOnlyProperty<Any, Flow<String>> =
    ReadOnlyProperty { _, _ -> getFlowString(key, defaultValue) }

fun AppDataStore.int(
    key: String, defaultValue: Int = 0
): ReadWriteProperty<Any, Int> =
    object : ReadWriteProperty<Any, Int> {
        override fun getValue(
            thisRef: Any,
            property: KProperty<*>
        ) = getInt(key, defaultValue)

        override fun setValue(
            thisRef: Any,
            property: KProperty<*>,
            value: Int
        ) = putInt(key, value)
    }

fun AppDataStore.flowInt(
    key: String,
    defaultValue: Int = 0
): ReadOnlyProperty<Any, Flow<Int>> =
    ReadOnlyProperty { _, _ -> getFlowInt(key, defaultValue) }

fun AppDataStore.float(
    key: String,
    defaultValue: Float = 0f
): ReadWriteProperty<Any, Float> =
    object : ReadWriteProperty<Any, Float> {
        override fun getValue(
            thisRef: Any,
            property: KProperty<*>
        ) = getFloat(key, defaultValue)

        override fun setValue(
            thisRef: Any,
            property: KProperty<*>,
            value: Float
        ) = putFloat(key, value)
    }

fun AppDataStore.flowFloat(
    key: String,
    defaultValue: Float = 0f
): ReadOnlyProperty<Any, Flow<Float>> =
    ReadOnlyProperty { _, _ -> getFlowFloat(key, defaultValue) }

fun AppDataStore.long(
    key: String, defaultValue: Long = 0L
): ReadWriteProperty<Any, Long> =
    object : ReadWriteProperty<Any, Long> {
        override fun getValue(
            thisRef: Any,
            property: KProperty<*>
        ) = getLong(key, defaultValue)

        override fun setValue(
            thisRef: Any,
            property: KProperty<*>,
            value: Long
        ) = putLong(key, value)
    }

fun AppDataStore.flowLong(
    key: String,
    defaultValue: Long = 0L
): ReadOnlyProperty<Any, Flow<Long>> =
    ReadOnlyProperty { _, _ -> getFlowLong(key, defaultValue) }

fun AppDataStore.boolean(
    key: String,
    defaultValue: Boolean = false
): ReadWriteProperty<Any, Boolean> =
    object : ReadWriteProperty<Any, Boolean> {
        override fun getValue(
            thisRef: Any,
            property: KProperty<*>
        ) = getBoolean(key, defaultValue)

        override fun setValue(
            thisRef: Any,
            property: KProperty<*>,
            value: Boolean
        ) = putBoolean(key, value)
    }

fun AppDataStore.flowBoolean(
    key: String,
    defaultValue: Boolean = false
): ReadOnlyProperty<Any, Flow<Boolean>> =
    ReadOnlyProperty { _, _ -> getFlowBoolean(key, defaultValue) }
