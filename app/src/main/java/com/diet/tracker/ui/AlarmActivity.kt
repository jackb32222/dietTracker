package com.diet.tracker.ui

import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.diet.tracker.R
import com.diet.tracker.notification.DietNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmActivity: AppCompatActivity() {

    @Inject lateinit var notificationManager: DietNotificationManager

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        notificationManager.showAlarmNotification()

        mediaPlayer = MediaPlayer.create(this, R.raw.sound)
        mediaPlayer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.release()
    }
}