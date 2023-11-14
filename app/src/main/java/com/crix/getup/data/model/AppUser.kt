package com.crix.getup.data.model

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot

data class AppUser(
    val userUUID: String,
    val userName: String,
    val profileImage: Int?,
    val chats: MutableList<Chat>
) {
    companion object {
        fun initFromFirebaseUser(firebaseUser: FirebaseUser): AppUser {
            return AppUser(
                firebaseUser.uid,
                "",
                null,
                mutableListOf())
        }

        fun fromFireBaseObject(snapshot: DocumentSnapshot, chats: MutableList<Chat>): AppUser {
            return AppUser(
                snapshot["uuid"] as String,
                snapshot["userName"] as String,
                snapshot["profileImage"] as Int?,
                chats,
            )
        }
    }

    fun toFireBaseObject(): HashMap<String, Any?> {
        return hashMapOf(
            "uuid" to userUUID,
            "userName" to userName,
            "profileImage" to profileImage
        )
    }
}