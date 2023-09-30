package com.crix.getup.ui.mainnavigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.crix.getup.databinding.FragmentProfileBinding
import com.crix.getup.ui.viewmodel.UserViewModel

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userViewModel.loggedInUser.observe(viewLifecycleOwner) {
            binding.tvUserName.text = it.userName
            // TODO: make this work
            binding.imUserImage.setImageResource(it.profileImage)
        }

        binding.btEditProfile.setOnClickListener {

        }

        binding.btAppSettings.setOnClickListener {

        }

        binding.btEditPreferences.setOnClickListener {

        }

        binding.btSendFeedback.setOnClickListener {

        }

        binding.btLogout.setOnClickListener {

        }
    }
}