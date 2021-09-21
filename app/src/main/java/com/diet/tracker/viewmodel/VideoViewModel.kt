package com.diet.tracker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diet.tracker.AppConstants
import com.diet.tracker.datasource.model.DietVideo
import com.diet.tracker.datasource.model.UserInfo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class VideoViewModel @Inject constructor(private val database: FirebaseDatabase) : ViewModel() {

    val userInfo = MutableLiveData<UserInfo>()
    val playlist = MutableLiveData<List<DietVideo>>()
    val bmrVideo = MutableLiveData<DietVideo>()

    fun getDynamicVideo() {
        val dynamicVideos = database.reference.child(AppConstants.NodeKey.DynamicVideos)
        dynamicVideos.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val videos = mutableListOf<DietVideo>()
                if (snapshot.key == AppConstants.NodeKey.DynamicVideos) {
                    snapshot.children.forEach { childSnapshot ->
                        childSnapshot.getValue(DietVideo::class.java)?.let { value ->
                            videos.add(value)
                            Log.d("nt.dung", "Video: ${value.url}")
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("nt.dung", "Error: ${error.message}")
            }
        })
    }

    fun saveUserInfo(userInfo: UserInfo) {
        this.userInfo.postValue(userInfo)
    }

    fun getPlaylist() {
        val videos = mutableListOf<DietVideo>()
        (0..30).forEach {
            videos.add(
                DietVideo(
                    url = "https://www.youtube.com/watch?v=fkF_SeC92Ss"
                )
            )
        }
        playlist.postValue(videos)
    }

    fun getBmrVideo() {
        bmrVideo.postValue(
            DietVideo(
                url = "https://www.youtube.com/watch?v=fkF_SeC92Ss"
            )
        )
    }
}