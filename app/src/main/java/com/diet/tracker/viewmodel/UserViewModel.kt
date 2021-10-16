package com.diet.tracker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diet.tracker.AppConstants
import com.diet.tracker.datasource.model.UserInfo
import com.diet.tracker.datasource.model.Weight
import com.google.firebase.database.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val database: FirebaseDatabase) : ViewModel() {

    val lvUser = MutableLiveData<UserInfo>()

    private var userValueListener = UserValueEventListener()
    private var userRef: DatabaseReference? = null

    fun getUserInfo(userId: String) {
        Log.d("nt.dung", "User Id: $userId")
        userRef = database.getReference(AppConstants.NodeKey.User).child(userId)
        userRef?.addValueEventListener(userValueListener)
    }

    fun saveUserInfo(userId: String, userInfo: UserInfo) {
        database.getReference(AppConstants.NodeKey.User)
            .child(userId)
            .setValue(userInfo)
            .addOnFailureListener {
                Log.e("nt.dung", "Save user info failed. ${it.message}")
            }
            .addOnSuccessListener {
                Log.d("nt.dung", "Save user successfully")
            }
    }

    fun saveUserCurrentDay(userId: String, currentDay: Long) {
        database.getReference(AppConstants.NodeKey.User)
            .child(userId)
            .child(AppConstants.NodeKey.Day)
            .setValue(currentDay)
            .addOnFailureListener {
                Log.e("nt.dung", "Save user info failed. ${it.message}")
            }
            .addOnSuccessListener {
                Log.d("nt.dung", "Save user successfully")
            }
    }

    inner class UserValueEventListener : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            try {
                val user: UserInfo? = snapshot.getValue(UserInfo::class.java)
                user?.let {
                    lvUser.postValue(it)
                }
            } catch (exc: Exception) {
                Log.e("nt.dung", "Exception: ${exc.message}")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("nt.dung", "Error: ${error.message}")
        }
    }

    override fun onCleared() {
        super.onCleared()
        userRef?.removeEventListener(userValueListener)
    }
}