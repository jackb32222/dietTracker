package com.diet.tracker.repository

import com.diet.tracker.datasource.model.Bmr
import com.diet.tracker.datasource.model.Meal
import kotlinx.coroutines.flow.Flow

interface DietRepo {

    fun setGoal(value: Int)
    fun flowGetGoal() : Flow<Int>

    fun setMeal(meal: Meal)
    fun setBmr(bmr: Bmr)

    fun flowGetMeal() : Flow<Meal>
    fun flowGetBmr() : Flow<Bmr>
}