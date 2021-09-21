package com.feature.firebase.database

import com.feature.firebase.database.model.NodeInfo
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference

typealias Node = DatabaseReference

interface RealtimeRepo {
    fun <T> setNodeValue(ref: Node, data: T)
    fun getRootNode(): Node
    fun getNode(parentNode: Node, key: String) : Node

    fun addNodeValueListener(
        node: Node,
        onChange: (nodeInfo: NodeInfo) -> Unit,
        onError: (exc: DatabaseError) -> Unit
    )

    fun addChildNodeValueListener(node: Node)
}