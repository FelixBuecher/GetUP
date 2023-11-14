package com.crix.getup.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.crix.getup.data.repository.UserRepository
import com.crix.getup.data.model.AppUser
import com.crix.getup.data.model.Chat
import com.crix.getup.data.model.Message
import java.time.LocalDate
import java.util.UUID

class UserViewModel: ViewModel() {

    private val repo = UserRepository()

    private val _loggedInUser = MutableLiveData<AppUser>(repo.fakeData())
    val loggedInUser get(): LiveData<AppUser> = _loggedInUser

    private val _currentChat = MutableLiveData<Chat>()
    val currentChat get(): LiveData<Chat> = _currentChat

    fun selectChat(chat: Chat) {
        _currentChat.value = chat
    }

    fun sendMessage(message: String) {
        currentChat.value?.messages?.add(
            Message(
                messageUUID = UUID.randomUUID().toString(),
                senderUUID = loggedInUser.value?.userUUID!!,
                message = message,
                sendTime = LocalDate.now(),
                seenBy = mutableListOf(loggedInUser.value?.userUUID!!)
            )
        )
    }


}