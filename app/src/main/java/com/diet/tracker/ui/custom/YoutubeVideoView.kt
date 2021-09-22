package com.diet.tracker.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.diet.tracker.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class YoutubeVideoView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_youtube_video, this, true)
    }

    private val youtubeView: YouTubePlayerView by lazy { findViewById(R.id.youtubeVideoView) }
    private var youtubePlayer: YouTubePlayer? = null

    private lateinit var videoId: String
    private val listener by lazy {
        object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youtubePlayer = youTubePlayer
                youtubePlayer?.cueVideo(videoId, 0f)
            }
        }
    }
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        findViewTreeLifecycleOwner()?.lifecycle?.addObserver(youtubeView)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        findViewTreeLifecycleOwner()?.lifecycle?.removeObserver(youtubeView)
    }

    fun setYoutubeVideoUrl(videoId: String?) {
        if (youtubePlayer != null) {
            videoId?.let {
                youtubePlayer?.cueVideo(videoId, 0f)
            }
        } else {
            videoId?.let {
                this.videoId = videoId
                youtubeView.enableAutomaticInitialization = false
                youtubeView.addYouTubePlayerListener(listener)
            }
        }
    }
}