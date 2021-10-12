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
import com.diet.tracker.datasource.model.DietVideo
import com.diet.tracker.datasource.model.Meal
import com.diet.tracker.datasource.model.UserInfo
import com.diet.tracker.notification.DietAlarmManager
import com.diet.tracker.ui.auth.SignInActivity
import com.diet.tracker.utils.convertToString
import com.diet.tracker.utils.getInt
import com.diet.tracker.viewmodel.AuthViewModel
import com.diet.tracker.viewmodel.DietViewModel
import com.diet.tracker.viewmodel.UserViewModel
import com.diet.tracker.viewmodel.VideoViewModel
import com.feature.firebase.auth.AuthManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class DietActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: DietViewModel by viewModels()
    private val videoViewModel: VideoViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    @Inject lateinit var alarmManager: DietAlarmManager
    @Inject lateinit var authManager: AuthManager

    private val mealLiveData by lazy { viewModel.getMeal() }
    private val mealObserver = Observer<Meal> {
        mealLiveData.removeObservers(this)

        with(binding) {
            inputMeal1.editText?.setText(it.meal1.convertToString())
            inputMeal2.editText?.setText(it.meal2.convertToString())
            inputMeal3.editText?.setText(it.meal3.convertToString())
            inputExercise.editText?.setText(it.exercise.convertToString())
            tvResult.text = String.format("Result: %d", getResult())
        }
    }

    private var userInfo: UserInfo? = null
    private var playlist: List<DietVideo>? = null

    private var nextEnable: Boolean = false
        set(value) {
            binding.btnNext.isEnabled = value
            field = value
        }
    private var prevEnable: Boolean = false
        set(value) {
            binding.btnPrev.isEnabled = value
            field = value
        }

    private var currentVideo: Int by Delegates.observable(0) { _, _, videoIdx ->
        playlist?.let { videos ->
            nextEnable = videoIdx < videos.size - 1
            prevEnable = videoIdx > 0
            val video = videos[videoIdx]
            binding.videoView.setYoutubeVideoUrl(video.getVideoId())
        }

        binding.tvDay.text = "Day ${videoIdx + 1}"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initViews()
        initListeners()
        initObservers()


        videoViewModel.getPlaylist()
        userViewModel.getUserInfo(authViewModel.getUserId())
    }

    private fun initObservers() {
        mealLiveData.observe(this, mealObserver)

        userViewModel.lvUser.observe(this) {
            this.userInfo = it
            currentVideo = userInfo?.day ?: 0
        }

        videoViewModel.playlist.observe(this) {
            this.playlist = it
            currentVideo = userInfo?.day ?: 0
        }
    }

    private fun initListeners() {
        binding.inputMeal1.editText?.addTextChangedListener { calculateCalories() }
        binding.inputMeal2.editText?.addTextChangedListener { calculateCalories() }
        binding.inputMeal3.editText?.addTextChangedListener { calculateCalories() }
        binding.inputExercise.editText?.addTextChangedListener { calculateCalories() }

        binding.btnSetGoal.setOnClickListener { setGoal() }
        binding.btnLogWeight.setOnClickListener {
            startActivity(Intent(this, WeightActivity::class.java))
        }
        binding.btnRefresh.setOnClickListener {
            refreshData()
        }
        binding.btnLogout.setOnClickListener {
            logout()
        }
        binding.btnNext.setOnClickListener {
            currentVideo++

            // Update current watching video to database
            val userInfo = UserInfo(currentVideo)
            userViewModel.saveUserInfo(authViewModel.getUserId(), userInfo)
        }
        binding.btnPrev.setOnClickListener {
            currentVideo--

            // Update current watching video to database
            val userInfo = UserInfo(currentVideo)
            userViewModel.saveUserInfo(authViewModel.getUserId(), userInfo)
        }
    }

    private fun initViews() {

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

    private fun logout() {
        authManager.logout()
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }
}