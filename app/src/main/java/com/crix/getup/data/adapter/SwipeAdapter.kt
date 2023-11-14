package com.crix.getup.data.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.crix.getup.data.model.Card
import com.crix.getup.databinding.CardItemBinding
import com.crix.getup.ui.mainnavigation.CardFragmentDirections

/**
 * Similar to a recycler view, we want to have a way to format the view based on our data.
 * This class is responsible for that.
 * @author Felix Bücher
 */
class SwipeAdapter(private var _dataset: MutableList<Card>): BaseAdapter() {

    private inner class SwipeItemViewHolder(view: View): ViewHolder(view) {
        val binding = CardItemBinding.bind(view)
    }



    override fun getCount(): Int {
        return _dataset.size
    }

    override fun getItem(position: Int): Card {
        return _dataset[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val card = _dataset[position]
        val binding: CardItemBinding = if (view == null) {
            CardItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        } else {
            CardItemBinding.bind(view)
        }
        val holder = SwipeItemViewHolder(binding.root)
        // Item content goes here
        holder.binding.ivCardIcon.setImageResource(card.img)
        holder.binding.cvCard.setOnClickListener {
            Log.d("TEST0", "NAVI")
            parent.findNavController().navigate(
                CardFragmentDirections.actionNavigationCardToCardDetailFragment()
            )
        }
        return binding.root
    }
}