package com.diet.tracker.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.diet.tracker.R
import com.diet.tracker.databinding.ActivityMainBinding
import com.diet.tracker.datasource.model.Meal
import com.diet.tracker.notification.DietAlarmManager
import com.diet.tracker.service.TimerService
import com.diet.tracker.ui.custom.TimerView
import com.diet.tracker.utils.getInt
import com.diet.tracker.viewmodel.DietViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DietActivity : AppCompatActivity() {

    companion object {
        const val VIDEO_URL = "gY_5rftmdZE"
    }

    private lateinit var binding: ActivityMainBinding

    private val viewModel: DietViewModel by viewModels()
    @Inject lateinit var alarmManager: DietAlarmManager

    private val mealLiveData by lazy { viewModel.getMeal() }
    private val mealObserver = Observer<Meal> {
        mealLiveData.removeObservers(this)
        binding.inputMeal1.editText?.setText(it.meal1.toString())
        binding.inputMeal2.editText?.setText(it.meal2.toString())
        binding.inputMeal3.editText?.setText(it.meal3.toString())
        binding.inputExercise.editText?.setText(it.exercise.toString())

        binding.tvResult.text = String.format("Result: %d", getResult())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.inputMeal1.editText?.addTextChangedListener { calculateCalories() }
        binding.inputMeal2.editText?.addTextChangedListener { calculateCalories() }
        binding.inputMeal3.editText?.addTextChangedListener { calculateCalories() }
        binding.inputExercise.editText?.addTextChangedListener { calculateCalories() }

        binding.videoView.setYoutubeVideoUrl(VIDEO_URL)
        binding.btnSetGoal.setOnClickListener { setGoal() }
        binding.btnCalculate.setOnClickListener { calculateCalories() }
        binding.btnRefresh.setOnClickListener { refreshData() }

        viewModel.getGoal().observe(this) {
            binding.tvGoal.text = String.format("Goal: %d", it)
        }

        mealLiveData.observe(this, mealObserver)
    }

    private fun refreshData() {
        binding.inputMeal1.editText?.setText("")
        binding.inputMeal2.editText?.setText("")
        binding.inputMeal3.editText?.setText("")
        binding.inputExercise.editText?.setText("")

        calculateCalories()
    }

    private fun setGoal() {
        viewModel.setGoal(value = getResult())
        startActivity(Intent(this, BmrActivity::class.java))
    }

    private fun calculateCalories() {
        binding.tvResult.text = String.format("Result: %d", getResult())

        viewModel.setMeal(
            Meal(
                binding.inputMeal1.getInt(),
                binding.inputMeal2.getInt(),
                binding.inputMeal3.getInt(),
                binding.inputExercise.getInt()
            )
        )
    }

    private fun getResult(): Int {
        return viewModel.calculateCalories(
            binding.inputMeal1.getInt(),
            binding.inputMeal2.getInt(),
            binding.inputMeal3.getInt(),
            binding.inputExercise.getInt()
        )
    }
}