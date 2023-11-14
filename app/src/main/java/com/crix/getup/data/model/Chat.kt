package com.crix.getup.data.model

import java.util.UUID

data class Chat(
    val chatUUID: String,
    val title: String,
    val members: MutableList<String>,
    val messages: MutableList<Message>
)