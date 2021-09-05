package com.diet.tracker.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import com.diet.tracker.R
import com.diet.tracker.ui.DietActivity
import kotlin.random.Random

class DietNotificationManager(private val context: Context) {

    companion object {
        const val DIET_CHANNEL_ID = "Diet"
        const val DIET_CHANNEL_NAME = "Diet Tracker timer"

        const val ALARM_CHANNEL_ID = "Diet alarm"
        const val ALARM_CHANNEL_NAME = "Diet alarm channel"
    }

    private val notificationManager = NotificationManagerCompat.from(context)
    private val timerNotificationBuilder = NotificationCompat.Builder(context, DIET_CHANNEL_ID)
    private val alarmNotificationBuilder = NotificationCompat.Builder(context, ALARM_CHANNEL_ID)

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(DIET_CHANNEL_ID, DIET_CHANNEL_NAME)
            createChannel(ALARM_CHANNEL_ID, ALARM_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(
        channelId: String,
        channelName: String,
        importance: Int = NotificationManager.IMPORTANCE_DEFAULT
    ) {
        val channel = NotificationChannel(channelId, channelName, importance)
        notificationManager.createNotificationChannel(channel)
    }

    fun showAlarmNotification() {
        val resultIntent = Intent(context, DietActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notification = alarmNotificationBuilder
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(context.getString(R.string.timer_message))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(Random.nextInt(), notification)
    }

    fun create(contentText: String): Notification {
        return timerNotificationBuilder
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(contentText)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }

    fun update(notificationId: Int, contentText: String) {
        timerNotificationBuilder
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(contentText)
            .setSmallIcon(R.mipmap.ic_launcher)
        return notificationManager.notify(notificationId, timerNotificationBuilder.build())
    }
}