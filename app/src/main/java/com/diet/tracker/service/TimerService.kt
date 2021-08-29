package com.diet.tracker.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.diet.tracker.notification.DietNotificationManager
import com.diet.tracker.receiver.TimerExpiredReceiver
import com.diet.tracker.utils.parseToString
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class TimerService : Service() {

    companion object {
        private const val START = "com.diet.tracker.START"
        private const val STOP = "com.diet.tracker.STOP"
        private const val RESET = "com.diet.tracker.RESET"

        private const val kTimerValue = "kTimerValue"
        private const val kNotificationId = 98765

        fun start(context: Context, timer: Long) {
            Intent(context, TimerService::class.java).apply {
                action = START
                putExtra(kTimerValue, timer)
                context.startService(this)
            }
        }

        fun stop(context: Context) {
            Intent(context, TimerService::class.java).apply {
                action = STOP
                context.startService(this)
            }
        }

        fun reset(context: Context) {
            Intent(context, TimerService::class.java).apply {
                action = RESET
                context.startService(this)
            }
        }
    }

    @Inject
    lateinit var notificationManager: DietNotificationManager
    private lateinit var timer: CountDownTimer
    private val binder: IBinder = LocalBinder()

    inner class LocalBinder : Binder() {
        val service: TimerService
            get() = this@TimerService
    }

    private var timerValue by Delegates.observable(0L) { _, _, value ->
        if (this::timer.isInitialized) {
            timer.cancel()
        }

        timer = DietTimer(value)
        timer.start()
        startForeground()
    }

    private var remainingTime: Long by Delegates.observable(0L) { _, _, value ->
        updateNotification()
        lvRemainingTime.value = value
    }

    private var timerFinished by Delegates.observable(false) { _, _, newValue ->
        if (newValue) {
            stopForeground(true)

            Intent(this, TimerExpiredReceiver::class.java).apply {
                action = TimerExpiredReceiver.ACTION_TIMER_EXPIRED
                sendBroadcast(this)
            }
        }
    }

    private var running by Delegates.observable(false) { _, _, newValue ->
        lvRunning.value = newValue
        if (!newValue) {
            if (this::timer.isInitialized) timer.cancel()
            stopForeground(true)
        }
    }

    val lvRemainingTime = MutableLiveData<Long>()
    val lvRunning = MutableLiveData<Boolean>()

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (intent.action) {
                START -> {
                    timerValue = it.getLongExtra(kTimerValue, 0)
                }
                STOP -> {
                    running = false
                }
                RESET -> {
                    timerValue = timerValue
                }
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        timerValue = 0L
    }

    private fun startForeground() {
        running = true
        startForeground(kNotificationId, notificationManager.create(timerValue.parseToString()))
    }

    private fun updateNotification() {
        notificationManager.update(kNotificationId, remainingTime.parseToString())
    }

    inner class DietTimer(millisInFuture: Long) : CountDownTimer(millisInFuture, 1_000L) {

        override fun onTick(millisUntilFinished: Long) {
            remainingTime = millisUntilFinished
        }

        override fun onFinish() {
            timerFinished = true
        }

    }
}