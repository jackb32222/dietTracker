package com.diet.tracker.datasource.model

data class UserInfo(
    var day: Int = 1,
    var weights: List<Weight> = defaultWeights()
) {
    companion object {
        fun defaultWeights() = (1..30).map { Weight(
            day = it,
            weight = 0.0
        ) }
    }
}
