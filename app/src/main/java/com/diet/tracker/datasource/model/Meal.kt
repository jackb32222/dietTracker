package com.diet.tracker.datasource.model


data class Meal(
    val meal1: Int,
    val meal2: Int,
    val meal3: Int,
    val exercise: Int
) {
    companion object {
        fun default() = Meal(0,0,0,0)
    }
}