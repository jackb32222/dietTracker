package com.diet.tracker.datasource.model

class Weight {
    var day: Int = 1
    var weight: Double = 0.0

    constructor() {}

    constructor(day: Int, weight: Double) {
        this.day = day
        this.weight = weight
    }
}