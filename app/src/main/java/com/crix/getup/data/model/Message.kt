package com.crix.getup.data.model

import java.time.LocalDate
import java.util.UUID

data class Message(
    val messageUUID: UUID,
    val senderUUID: UUID,
    val message: String,
    val sendTime: LocalDate,
    val seenBy: MutableList<UUID> = mutableListOf()
)