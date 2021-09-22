package com.diet.tracker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diet.tracker.AppConstants
import com.diet.tracker.datasource.model.DietVideo
import com.diet.tracker.datasource.model.UserInfo
import com.google.firebase.database.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class VideoViewModel @Inject constructor(private val database: FirebaseDatabase) : ViewModel() {

    val userInfo = MutableLiveData<UserInfo>()
    val playlist = MutableLiveData<List<DietVideo>>()
    val bmrVideo = MutableLiveData<DietVideo>()

    private val playlistRef: DatabaseReference by lazy { database.getReference(AppConstants.NodeKey.DynamicVideos) }
    private val playlistValueListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val videos = mutableListOf<DietVideo>()
            snapshot.children.forEach { childSnapshot ->
                childSnapshot.getValue(DietVideo::class.java)?.let { value ->
                    videos.add(value)
                }
            }

            playlist.postValue(videos)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("nt.dung", "Error: ${error.message}")
        }
    }

    private val fixVideoRef: DatabaseReference by lazy { database.getReference(AppConstants.NodeKey.FixVideo) }
    private val fixVideoValueListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            snapshot.children.forEach { childSnapshot ->
                childSnapshot.getValue(DietVideo::class.java)?.let { value ->
                    bmrVideo.postValue(value)
                }
            }

        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }
    }

    private var userValueListener: UserValueEventListener? = null
    private var userRef: DatabaseReference? = null

    fun getUserInfo(userId: String) {
        userRef = database.getReference(AppConstants.NodeKey.User).child(userId)
        userValueListener = UserValueEventListener(userId)
        userValueListener?.let {
            userRef?.addValueEventListener(it)
        }
    }

    fun getPlaylist() {
        playlistRef.addValueEventListener(playlistValueListener)
    }

    fun saveUserInfo(userInfo: UserInfo) {
        database.getReference(AppConstants.NodeKey.User)
            .child(userInfo.id)
            .setValue(userInfo.day)
            .addOnFailureListener {
                Log.e("nt.dung", "Save user info failed. ${it.message}")
            }
            .addOnSuccessListener {
                Log.d("nt.dung", "Save user successfully")
            }
    }

    fun getBmrVideo() {
        fixVideoRef.addValueEventListener(fixVideoValueListener)
    }

    override fun onCleared() {
        super.onCleared()
        playlistRef.removeEventListener(playlistValueListener)
        fixVideoRef.removeEventListener(fixVideoValueListener)
        userValueListener?.let {
            userRef?.removeEventListener(it)
        }
    }

    inner class UserValueEventListener(private val userId: String) : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.value is Long) {
                val day: Int = (snapshot.value as Long).toInt()
                Log.d("nt.dung", "User: $userId, day: $day")
                userInfo.postValue(UserInfo(userId, day))
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("nt.dung", "Error: ${error.message}")
        }
    }
}