package com.crix.getup.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.crix.getup.data.model.Message
import com.crix.getup.databinding.IncomingMessageItemBinding
import com.crix.getup.databinding.OutgoingMessageItemBinding
import com.crix.getup.ui.viewmodel.UserViewModel

class IndividualChatAdapter(
    private var messages: MutableList<Message>,
    private val userViewModel: UserViewModel,
) : RecyclerView.Adapter<ViewHolder>() {

    inner class IncomingViewHolder(val binding: IncomingMessageItemBinding) :
        ViewHolder(binding.root)

    inner class OutgoingViewHolder(val binding: OutgoingMessageItemBinding) :
        ViewHolder(binding.root)

    private val INCOMINGTYPE = 0
    private val OUTGOINGTYPE = 1

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].senderUUID == userViewModel.loggedInUser.value?.userUUID) OUTGOINGTYPE else INCOMINGTYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == OUTGOINGTYPE) {
            val binding = OutgoingMessageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            OutgoingViewHolder(binding)
        } else {
            val binding = IncomingMessageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            IncomingViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is OutgoingViewHolder) {
            holder.binding.tvMessage.text = message.message
        } else if (holder is IncomingViewHolder) {
            holder.binding.tvMessage.text = message.message
        }
    }
}