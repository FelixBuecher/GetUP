package com.crix.getup.data.model

import java.util.UUID

data class Chat(
    val chatUUID: UUID,
    val title: String,
    val members: MutableList<UUID>,
    val messages: MutableList<Message>
)