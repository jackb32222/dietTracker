package com.diet.tracker.ui

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.diet.tracker.R
import com.diet.tracker.databinding.ActivityMainBinding
import com.diet.tracker.datasource.model.Meal
import com.diet.tracker.service.TimerService
import com.diet.tracker.ui.custom.TimerView
import com.diet.tracker.utils.getInt
import com.diet.tracker.viewmodel.DietViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DietActivity : AppCompatActivity(), TimerView.OnChanged {

    companion object {
        const val VIDEO_URL = "gY_5rftmdZE"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var timerService: TimerService

    private val viewModel: DietViewModel by viewModels()
    private var serviceBound = false

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            serviceBound = true
            timerService = (service as TimerService.LocalBinder).service

            timerService.lvRunning.observe(this@DietActivity) {
                binding.timerView.isRunning = it
            }

            timerService.lvRemainingTime.observe(this@DietActivity) {
                binding.timerView.remainingTime = it
            }
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            serviceBound = false
        }
    }

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

        binding.timerView.onChangeListener = this

        viewModel.getGoal().observe(this) {
            binding.tvGoal.text = String.format("Goal: %d", it)
        }

        mealLiveData.observe(this, mealObserver)

        Intent(this, TimerService::class.java).apply {
            bindService(this, serviceConnection, BIND_AUTO_CREATE)
        }
    }

    private fun refreshData() {
        binding.inputMeal1.editText?.setText("")
        binding.inputMeal2.editText?.setText("")
        binding.inputMeal3.editText?.setText("")
        binding.inputExercise.editText?.setText("")

        calculateCalories()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (serviceBound) {
            unbindService(serviceConnection)
            serviceBound = false
        }
    }

    override fun onTimerStart(timerValue: Long) {
        TimerService.start(this, timerValue)
    }

    override fun onTimerPause() {
        TimerService.stop(this)
    }

    override fun onTimerReset() {
        TimerService.reset(this)
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