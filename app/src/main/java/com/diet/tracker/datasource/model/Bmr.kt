package com.diet.tracker.datasource.model

data class Bmr(
    val age: Int,
    val height: Double,
    val weight: Double,
    val isFemale: Boolean
) {
    companion object {
        fun default() = Bmr(0, 0.0, 0.0, false)
    }
}