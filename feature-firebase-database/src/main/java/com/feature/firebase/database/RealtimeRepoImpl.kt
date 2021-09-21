package com.feature.firebase.database

import com.feature.firebase.database.model.NodeInfo
import com.google.firebase.database.*

class RealtimeRepoImpl(private val database: FirebaseDatabase) : RealtimeRepo {
    override fun <T> setNodeValue(ref: Node, data: T) {
        ref.setValue(data)
    }

    override fun getRootNode() = database.reference

    override fun getNode(parentNode: Node, key: String) = parentNode.child(key)

    override fun addNodeValueListener(
        node: Node,
        onChange: (nodeInfo: NodeInfo) -> Unit,
        onError: (exc: DatabaseError) -> Unit
    ) {
        node.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                onChange.invoke(NodeInfo(node.key, snapshot))
            }

            override fun onCancelled(error: DatabaseError) {
                onError.invoke(error)
            }
        })
    }

    override fun addChildNodeValueListener(node: Node) {
        node.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}