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
        val chocolateCalories = 230.0

        val bmr = if (female) {
            655 + (4.3 * weight) + (4.7 * height) - (4.7 * age)
        } else {
            66 + (6.3 * weight) + (12.9 * height) - (6.8 * age)
        }

        return (bmr / chocolateCalories).roundToInt()
    }

    fun calculateCalories(meal1: Int, meal2: Int, meal3: Int, exercise: Int) =
        listOf(meal1, meal2, meal3).sum() - exercise

    fun getGoal() = repo.flowGetGoal().asLiveData()

    fun setGoal(value: Int) = repo.setGoal(value)
}