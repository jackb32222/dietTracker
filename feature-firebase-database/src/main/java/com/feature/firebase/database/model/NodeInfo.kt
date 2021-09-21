package com.feature.firebase.database.model

import com.google.firebase.database.DataSnapshot

data class NodeInfo(
    var key: String? = null,
    var snapshot: DataSnapshot? = null
)