package com.crix.getup.data.model

import java.time.LocalDate
import java.util.UUID

data class Message(
    val messageUUID: String,
    val senderUUID: String,
    val message: String,
    val sendTime: LocalDate,
    val seenBy: MutableList<String> = mutableListOf()
)