package com.diet.tracker.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.diet.tracker.R
import com.diet.tracker.databinding.ActivityBmrBinding
import com.diet.tracker.datasource.model.Bmr
import com.diet.tracker.utils.convertToString
import com.diet.tracker.utils.getDouble
import com.diet.tracker.utils.getInt
import com.diet.tracker.viewmodel.DietViewModel
import com.diet.tracker.viewmodel.VideoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BmrActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBmrBinding
    private val viewModel: DietViewModel by viewModels()
    private val videoViewModel: VideoViewModel by viewModels()

    private val bmrLiveData by lazy { viewModel.getBmr() }
    private val bmrObserver = Observer<Bmr> {
        bmrLiveData.removeObservers(this)
        binding.inputAge.editText?.setText(it.age.convertToString())
        binding.inputWeight.editText?.setText(it.weight.toString())
        binding.inputHeight.editText?.setText(it.height.toString())
        binding.rdoFemale.isChecked = it.isFemale

        binding.tvResult.text = String.format("Result: %d", getResult())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bmr)
        binding.btnCalculate.setOnClickListener { calculateBmr() }
        binding.btnRefresh.setOnClickListener { refresh() }

        binding.inputAge.editText?.addTextChangedListener { calculateBmr() }
        binding.inputWeight.editText?.addTextChangedListener { calculateBmr() }
        binding.inputHeight.editText?.addTextChangedListener { calculateBmr() }
        binding.gender.setOnCheckedChangeListener { _, _ -> calculateBmr() }
        binding.btnSetGoal.setOnClickListener { setGoal() }

        viewModel.getGoal().observe(this) {
            binding.tvGoal.text = String.format("Goal: %d", it)
        }

        bmrLiveData.observe(this, bmrObserver)

        videoViewModel.bmrVideo.observe(this) {
            binding.videoView.setYoutubeVideoUrl(it.getVideoId())
        }

        videoViewModel.getBmrVideo()
    }

    private fun refresh() {
        binding.inputAge.editText?.setText("")
        binding.inputHeight.editText?.setText("")
        binding.inputWeight.editText?.setText("")

        calculateBmr()
    }

    private fun setGoal() {
        viewModel.setGoal(value = getResult())
        finish()
    }

    private fun calculateBmr() {
        binding.tvResult.text = String.format("Result: %d", getResult())

        viewModel.setBmr(Bmr(
            binding.inputAge.getInt(),
            binding.inputWeight.getDouble(),
            binding.inputHeight.getDouble(),
            binding.rdoFemale.isChecked
        ))
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