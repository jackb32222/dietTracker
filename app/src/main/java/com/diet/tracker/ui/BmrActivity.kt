package com.diet.tracker.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.diet.tracker.R
import com.diet.tracker.databinding.ActivityBmrBinding
import com.diet.tracker.utils.getDouble
import com.diet.tracker.utils.getInt
import com.diet.tracker.viewmodel.DietViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BmrActivity : AppCompatActivity() {

    companion object {
        const val VIDEO_URL = "gY_5rftmdZE"
    }
    private lateinit var binding: ActivityBmrBinding
    private val viewModel: DietViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bmr)
        binding.btnCalculate.setOnClickListener { calculateBmr() }

        binding.inputAge.editText?.addTextChangedListener { calculateBmr() }
        binding.inputWeight.editText?.addTextChangedListener { calculateBmr() }
        binding.inputHeight.editText?.addTextChangedListener { calculateBmr() }
        binding.gender.setOnCheckedChangeListener { _, _ -> calculateBmr() }

        binding.videoView.setYoutubeVideoUrl(VIDEO_URL)
        binding.btnSetGoal.setOnClickListener { setGoal() }

        viewModel.getGoal().observe(this) {
            binding.tvGoal.text = String.format("Goal: %d", it)
        }

        calculateBmr()
    }

    private fun setGoal() {
        viewModel.setGoal(value = getResult())
        finish()
    }

    private fun calculateBmr() {
        binding.tvResult.text = String.format("Result: %d", getResult())
    }

    private fun getResult() : Int {
        return viewModel.calculateBmr(
            binding.inputAge.getInt(),
            binding.inputWeight.getDouble(),
            binding.inputHeight.getDouble(),
            binding.rdoFemale.isChecked
        )
    }
}