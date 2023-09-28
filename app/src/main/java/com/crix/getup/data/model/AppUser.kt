package com.crix.getup.data.model

import java.util.UUID

data class AppUser(
    val userUUID: UUID,
    val userName: String,
    val profileImage: Int,
    val chats: MutableList<Chat>
)