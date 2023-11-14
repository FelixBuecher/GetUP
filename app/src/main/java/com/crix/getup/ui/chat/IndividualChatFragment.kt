package com.crix.getup.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.crix.getup.data.adapter.IndividualChatAdapter
import com.crix.getup.databinding.FragmentIndividualChatBinding
import com.crix.getup.ui.viewmodel.UserViewModel

class IndividualChatFragment : Fragment() {

    private lateinit var binding: FragmentIndividualChatBinding
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIndividualChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val rv = binding.rvChat
        userViewModel.currentChat.observe(viewLifecycleOwner) {
            rv.adapter = IndividualChatAdapter(it.messages, userViewModel)
        }

        binding.btSend.setOnClickListener {
            val message = binding.tvMessageField.text.trim().toString()

            if(message.isNotEmpty()) {
                userViewModel.sendMessage(message)
            }

            binding.tvMessageField.text.clear()
        }

    }
}