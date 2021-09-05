package com.diet.tracker.ui.custom

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import com.diet.tracker.R
import com.diet.tracker.datasource.model.Timer
import com.diet.tracker.notification.DietAlarmManager
import com.diet.tracker.repository.DietRepo
import com.diet.tracker.utils.isEnable
import com.diet.tracker.utils.parseToString
import com.diet.tracker.utils.text
import com.google.android.material.slider.Slider
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class TimerView(context: Context, attrs: AttributeSet?) : RelativeLayout(context, attrs),
    Slider.OnChangeListener {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_timer, this, true)
    }

    private val btnStart: View by lazy { findViewById(R.id.btnStart) }
    private val btnPause: View by lazy { findViewById(R.id.btnPause) }
    private val btnReset: View by lazy { findViewById(R.id.btnReset) }
    private val sliderTimerValue: Slider by lazy { findViewById(R.id.sliderTimerValue) }
    private val tvRemainingTime: TextView by lazy { findViewById(R.id.tvRemainingTime) }

    private var btnStartEnable by btnStart.isEnable()
    private var btnPauseEnable by btnPause.isEnable()
    private var btnResetEnable by btnReset.isEnable()
    private var sliderEnable by sliderTimerValue.isEnable()
    private var remainingText by tvRemainingTime.text()

    @Inject
    lateinit var alarmManager: DietAlarmManager

    @Inject
    lateinit var repo: DietRepo

    private lateinit var timer: CountDownTimer

    var isRunning by Delegates.observable(false) { _, _, running ->
        btnStartEnable = !running
        btnPauseEnable = running
        btnResetEnable = running
        sliderEnable = !running
    }

    var remainingTime by Delegates.observable(0L) { _, _, remainMs ->
        sliderTimerValue.value = remainMs.toFloat()
    }

    val liveDataTimer by lazy { repo.getTimer() }

    private lateinit var currentTimer: Timer

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        sliderTimerValue.apply {
            valueFrom = 0f
            valueTo = TimeUnit.HOURS.toMillis(8).toFloat()
        }

        btnStart.setOnClickListener { start(durationMs()) }
        btnPause.setOnClickListener { pause() }
        btnReset.setOnClickListener { reset() }
        sliderTimerValue.addOnChangeListener(this)

        initTimer()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        btnStart.setOnClickListener(null)
        btnPause.setOnClickListener(null)
        btnReset.setOnClickListener(null)
        sliderTimerValue.removeOnChangeListener(this)
    }

    private fun initTimer() {
        liveDataTimer.observeForever(object : Observer<Timer> {
            override fun onChanged(timer: Timer) {
                liveDataTimer.removeObserver(this)
                isRunning = timer.isRunning()
                remainingTime = timer.getRemainingTime()

                if (isRunning) start(remainingTime, resume = true)
            }
        })
    }

    private fun start(durationMs: Long, resume: Boolean = false) {
        if (this::timer.isInitialized) {
            timer.cancel()
        }

        timer = DietTimer(durationMs)
        timer.start()

        isRunning = true
        if (!resume) {
            Timer(durationMs, System.currentTimeMillis() + durationMs).apply {
                repo.saveTimer(this)
                alarmManager.start(this)
                currentTimer = this
            }
        }
    }

    private fun pause() {
        timer.cancel()
        alarmManager.pause()
        isRunning = false
    }

    private fun reset() {
        isRunning = true
        if (this::currentTimer.isInitialized) {
            start(currentTimer.durationMs)
        }
    }

    private fun durationMs() = 30_000L //sliderTimerValue.value.toLong()

    override fun onValueChange(slider: Slider, value: Float, fromUser: Boolean) {
        remainingText = value.toLong().parseToString()
    }

    inner class DietTimer(millisInFuture: Long) : CountDownTimer(millisInFuture, 1_000L) {

        override fun onTick(millisUntilFinished: Long) {
            remainingTime = millisUntilFinished

        }

        override fun onFinish() {
            remainingTime = 0
        }

    }
}