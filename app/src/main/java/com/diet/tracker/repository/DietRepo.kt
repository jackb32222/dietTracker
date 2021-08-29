package com.diet.tracker.repository

import kotlinx.coroutines.flow.Flow

interface DietRepo {

    fun setGoal(value: Int)
    fun flowGetGoal() : Flow<Int>
}