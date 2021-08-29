package com.diet.tracker.repository

import com.diet.tracker.datasource.local.AppDataStore
import kotlinx.coroutines.flow.Flow

class DietRepoImpl(private val datastore: AppDataStore) : DietRepo {

    companion object {
        const val kGoal = "kGoal"
    }

    override fun setGoal(value: Int) {
        datastore.putInt(kGoal, value)
    }

    override fun flowGetGoal() = datastore.getFlowInt(kGoal, 0)
}