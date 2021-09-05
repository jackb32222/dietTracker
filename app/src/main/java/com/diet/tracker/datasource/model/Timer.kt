package com.diet.tracker.datasource.model

import kotlin.math.max

data class Timer(val durationMs: Long, val triggerMs: Long) {
    companion object {
        fun default() = Timer(0, 0)
    }

    fun isRunning() = System.currentTimeMillis() < triggerMs
    fun getRemainingTime() = max(triggerMs - System.currentTimeMillis(), 0)
}