package com.diet.tracker.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.diet.tracker.datasource.model.Timer
import com.diet.tracker.receiver.TimerExpiredReceiver
import com.diet.tracker.ui.AlarmActivity

class DietAlarmManager(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

    fun start(timer: Timer) {
        alarmManager?.cancel(getPendingIntent())
        alarmManager?.setExact(AlarmManager.RTC_WAKEUP, timer.triggerMs, getPendingIntent())
    }

    fun pause() {
        alarmManager?.cancel(getPendingIntent())
    }

    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(context, AlarmActivity::class.java)
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}