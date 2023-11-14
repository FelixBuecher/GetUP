package com.crix.getup.ui.mainnavigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.crix.getup.databinding.FragmentCardBinding
import com.crix.getup.ui.viewmodel.CardViewModel
import com.crix.getup.data.adapter.SwipeAdapter
import com.crix.getup.util.swipe.SwipeStack

val TAG = "CardFragment"

class CardFragment : Fragment(), SwipeStack.SwipeStackListener {

    private lateinit var binding: FragmentCardBinding
    private val viewModel: CardViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.swipeStack.setListener(this)
        viewModel.cards.observe(viewLifecycleOwner) {
            binding.swipeStack.adapter = SwipeAdapter(it)
        }
    }

    override fun onViewSwipedLeft(position: Int) {
        viewModel.swipedLeft(position)
    }

    override fun onViewSwipedRight(position: Int) {
        viewModel.swipedRight(position)
    }

    override fun onStackEmpty() {
        viewModel.reloadCards()
        binding.swipeStack.resetStack()
    }
}