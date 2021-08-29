package com.diet.tracker.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.diet.tracker.notification.DietNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TimerExpiredReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_TIMER_EXPIRED = "com.diet.tracker.TIMER_EXPIRED"
    }

    @Inject lateinit var notificationManager: DietNotificationManager

    override fun onReceive(context: Context?, intent: Intent?) {
        notificationManager.showAlarmNotification()
    }
}