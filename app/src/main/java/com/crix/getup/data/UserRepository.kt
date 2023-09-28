package com.crix.getup.data

import com.crix.getup.data.model.AppUser
import com.crix.getup.data.model.Chat
import com.crix.getup.data.model.Message
import java.time.LocalDate
import java.util.UUID

class UserRepository {
    fun fakeData(): AppUser {
        val myUUID = UUID.randomUUID()
        return AppUser(
            userUUID = myUUID,
            userName = "Felix",
            profileImage = 0,
            chats = mutableListOf(
                Chat(
                    chatUUID = UUID.randomUUID(),
                    title = "Zocken",
                    members = mutableListOf(
                        myUUID
                    ),
                    messages = mutableListOf(
                        Message(
                            messageUUID = UUID.randomUUID(),
                            senderUUID = myUUID,
                            message = "Das ist ein test",
                            sendTime = LocalDate.now(),
                            seenBy = mutableListOf(
                                myUUID
                            )
                        )
                    )
                ),
                Chat(
                    chatUUID = UUID.randomUUID(),
                    title = "Schwimmen",
                    members = mutableListOf(
                        myUUID
                    ),
                    messages = mutableListOf(
                        Message(
                            messageUUID = UUID.randomUUID(),
                            senderUUID = UUID.randomUUID(),
                            message = "ganz",
                            sendTime = LocalDate.now(),
                        ),
                        Message(
                            messageUUID = UUID.randomUUID(),
                            senderUUID = UUID.randomUUID(),
                            message = "spät",
                            sendTime = LocalDate.now(),
                        ),
                        Message(
                            messageUUID = UUID.randomUUID(),
                            senderUUID = UUID.randomUUID(),
                            message = "diggi",
                            sendTime = LocalDate.now(),
                        )
                    )
                )
            )
        )
    }
}