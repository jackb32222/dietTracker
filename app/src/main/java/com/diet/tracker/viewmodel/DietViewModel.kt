package com.diet.tracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.diet.tracker.repository.DietRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class DietViewModel @Inject constructor(private val repo: DietRepo) : ViewModel() {

    fun calculateBmr(age: Int, weight: Double, height: Double, female: Boolean) : Int {
        val bmr = if (female) {
            655.1 + (9.563 * weight) + (1.85 * height) - (4.676 * age)
        } else {
            66.47 + (13.75 * weight) + (5.003 * height) - (6.755 * age)
        }

        return bmr.roundToInt()
    }

    fun calculateCalories(meal1: Int, meal2: Int, meal3: Int, exercise: Int) =
        listOf(meal1, meal2, meal3).sum() - exercise

    fun getGoal() = repo.flowGetGoal().asLiveData()

    fun setGoal(value: Int) = repo.setGoal(value)
}