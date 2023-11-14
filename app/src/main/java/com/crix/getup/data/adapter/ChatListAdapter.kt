package com.crix.getup.data.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.crix.getup.data.model.Chat
import com.crix.getup.databinding.ChatListItemBinding
import com.crix.getup.ui.mainnavigation.ChatFragmentDirections
import com.crix.getup.ui.viewmodel.UserViewModel

class ChatListAdapter(
    private var chats: MutableList<Chat>,
    private val userViewModel: UserViewModel,
): RecyclerView.Adapter<ChatListAdapter.ChatListItemViewHolder>() {

    init {
        chats.sortBy {
                chat -> chat.messages.any {
                message -> message.seenBy.contains(userViewModel.loggedInUser.value?.userUUID)
                }
        }
    }

    inner class ChatListItemViewHolder(val binding: ChatListItemBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListItemViewHolder {
        val binding = ChatListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatListItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    override fun onBindViewHolder(holder: ChatListItemViewHolder, position: Int) {
        val chat = chats[position]
        val binding = holder.binding
        // Set the chats title
        binding.tvChatTitle.text = chat.title
        // If chat is not empty, show last message
        if(chat.messages.isNotEmpty()) {
            binding.tvLastMessage.text = chat.messages.last().message
            // TODO: Show "yesterday" and so on, calc time
            binding.tvLastSent.text = chat.messages.last().sendTime.toString()

            // If one of the messages received is unread, tint the card green
            if(chat.messages.any { !it.seenBy.contains(userViewModel.loggedInUser.value?.userUUID) }) {
                binding.ivNotification.visibility = View.VISIBLE
            }
        }

        binding.cvChatCard.setOnClickListener {
            userViewModel.selectChat(chat)
            holder.itemView.findNavController().navigate(ChatFragmentDirections.actionNavigationChatToIndividualChatFragment())
        }

    }
}