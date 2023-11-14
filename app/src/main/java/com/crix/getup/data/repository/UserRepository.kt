package com.crix.getup.data.repository

import com.crix.getup.data.model.AppUser
import com.crix.getup.data.model.Chat
import com.crix.getup.data.model.Message
import java.time.LocalDate
import java.util.UUID

class UserRepository {
    fun fakeData(): AppUser {
        val myUUID = UUID.randomUUID().toString()
        return AppUser(
            userUUID = myUUID,
            userName = "Felix",
            profileImage = 0,
            chats = mutableListOf(
                Chat(
                    chatUUID = UUID.randomUUID().toString(),
                    title = "Zocken",
                    members = mutableListOf(
                        myUUID
                    ),
                    messages = mutableListOf(
                        Message(
                            messageUUID = UUID.randomUUID().toString(),
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
                    chatUUID = UUID.randomUUID().toString(),
                    title = "Schwimmen",
                    members = mutableListOf(
                        myUUID
                    ),
                    messages = mutableListOf(
                        Message(
                            messageUUID = UUID.randomUUID().toString(),
                            senderUUID = UUID.randomUUID().toString(),
                            message = "ganz",
                            sendTime = LocalDate.now(),
                        ),
                        Message(
                            messageUUID = UUID.randomUUID().toString(),
                            senderUUID = UUID.randomUUID().toString(),
                            message = "spät",
                            sendTime = LocalDate.now(),
                        ),
                        Message(
                            messageUUID = UUID.randomUUID().toString(),
                            senderUUID = UUID.randomUUID().toString(),
                            message = "diggi",
                            sendTime = LocalDate.now(),
                        )
                    )
                )
            )
        )
    }
}