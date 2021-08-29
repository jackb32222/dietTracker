package com.diet.tracker.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.diet.tracker.R
import com.diet.tracker.utils.isEnable
import com.diet.tracker.utils.parseToString
import com.diet.tracker.utils.text
import com.google.android.material.slider.Slider
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class TimerView(context: Context, attrs: AttributeSet?) : RelativeLayout(context, attrs),
    Slider.OnChangeListener {

    interface OnChanged {
        fun onTimerStart(timerValue: Long)
        fun onTimerPause()
        fun onTimerReset()
    }

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

    var onChangeListener: OnChanged? = null

    var isRunning by Delegates.observable(false) { _, _, running ->
        btnStartEnable = !running
        btnPauseEnable = running
        btnResetEnable = running
        sliderEnable = !running
    }

    var remainingTime by Delegates.observable(0L) { _, _, newValue ->
        sliderTimerValue.value = newValue.toFloat()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        sliderTimerValue.apply {
            valueFrom = 0f
            valueTo = TimeUnit.HOURS.toMillis(8).toFloat()
        }

        btnStart.setOnClickListener { start() }
        btnPause.setOnClickListener { pause() }
        btnReset.setOnClickListener { reset() }
        sliderTimerValue.addOnChangeListener(this)

        remainingText = sliderTimerValue.value.toLong().parseToString()
        isRunning = false
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        btnStart.setOnClickListener(null)
        btnPause.setOnClickListener(null)
        btnReset.setOnClickListener(null)
        sliderTimerValue.removeOnChangeListener(this)
    }

    private fun start() {
        onChangeListener?.onTimerStart(sliderTimerValue.value.toLong())
    }

    private fun pause() {
        onChangeListener?.onTimerPause()
    }

    private fun reset() {
        onChangeListener?.onTimerReset()
    }

    override fun onValueChange(slider: Slider, value: Float, fromUser: Boolean) {
        remainingText = value.toLong().parseToString()
    }
}