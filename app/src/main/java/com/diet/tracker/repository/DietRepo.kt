package com.diet.tracker.repository

import androidx.lifecycle.LiveData
import com.diet.tracker.datasource.model.Bmr
import com.diet.tracker.datasource.model.Meal
import com.diet.tracker.datasource.model.Timer
import kotlinx.coroutines.flow.Flow

interface DietRepo {

    fun setGoal(value: Int)
    fun flowGetGoal() : Flow<Int>

    fun setMeal(meal: Meal)
    fun setBmr(bmr: Bmr)
    fun saveTimer(timer: Timer)

    fun flowGetMeal() : Flow<Meal>
    fun flowGetBmr() : Flow<Bmr>
    fun getTimer() : LiveData<Timer>
}