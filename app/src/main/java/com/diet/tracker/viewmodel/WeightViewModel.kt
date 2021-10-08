package com.diet.tracker.viewmodel

import androidx.lifecycle.ViewModel
import com.diet.tracker.AppConstants
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeightViewModel @Inject constructor(private val database: FirebaseDatabase) : ViewModel() {

    private val weightRef: DatabaseReference by lazy { database.getReference(AppConstants.NodeKey.FixVideo) }
}