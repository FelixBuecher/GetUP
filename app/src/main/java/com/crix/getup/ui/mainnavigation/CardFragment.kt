package com.crix.getup.ui.mainnavigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.crix.getup.databinding.FragmentCardBinding
import com.crix.getup.ui.viewmodel.CardViewModel
import com.crix.getup.util.swipe.SwipeAdapter
import com.crix.getup.util.swipe.SwipeStack

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
        val swipeStack = binding.swipeStack
        swipeStack.setListener(this)
        viewModel.cards.observe(viewLifecycleOwner) {
            swipeStack.adapter = SwipeAdapter(viewModel.cards.value!!)
        }


    }

    override fun onViewSwipedLeft(position: Int) {

    }

    override fun onViewSwipedRight(position: Int) {

    }

    override fun onStackEmpty() {

    }

    override fun onStackNearEmpty() {
        viewModel.reloadCards()
    }
}