package com.diet.tracker.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.diet.tracker.datasource.local.AppDataStore
import com.diet.tracker.datasource.model.Bmr
import com.diet.tracker.datasource.model.Meal
import com.diet.tracker.datasource.model.Timer
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

class DietRepoImpl(private val datastore: AppDataStore) : DietRepo {

    companion object {
        const val kGoal = "kGoal"
        const val kMeal = "kMeal"
        const val kBmr = "kBmr"
        const val kTimer = "kTimer"
    }

    override fun setGoal(value: Int) {
        datastore.putInt(kGoal, value)
    }

    override fun setMeal(meal: Meal) {
        datastore.putString(kMeal, Gson().toJson(meal))
    }

    override fun setBmr(bmr: Bmr) {
        datastore.putString(kBmr, Gson().toJson(bmr))
    }

    override fun saveTimer(timer: Timer) {
        datastore.putString(kTimer, Gson().toJson(timer))
    }

    override fun flowGetMeal(): Flow<Meal> {
        return datastore.getFlowString(kMeal, "")
            .filter { it.isNotEmpty() }
            .map { Gson().fromJson(it, Meal::class.java) }
    }

    override fun flowGetBmr(): Flow<Bmr> {
        return datastore.getFlowString(kBmr, "")
            .filter { it.isNotEmpty() }
            .map { Gson().fromJson(it, Bmr::class.java) }
    }

    override fun getTimer(): LiveData<Timer> {
        return datastore.getFlowString(kTimer, Gson().toJson(Timer.default()))
            .map { Gson().fromJson(it, Timer::class.java) }
            .asLiveData()
    }

    override fun flowGetGoal() = datastore.getFlowInt(kGoal, 0)
}