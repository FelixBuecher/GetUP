package com.crix.getup.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.crix.getup.data.adapter.ChatListAdapter
import com.crix.getup.databinding.FragmentChatBinding
import com.crix.getup.ui.viewmodel.UserViewModel

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val rv = binding.rvChatList
        userViewModel.loggedInUser.observe(viewLifecycleOwner) {
            val adapter = ChatListAdapter(it.chats, userViewModel)
            rv.adapter = adapter
        }
    }
}