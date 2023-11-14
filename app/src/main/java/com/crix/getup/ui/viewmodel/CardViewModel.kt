package com.crix.getup.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.crix.getup.data.repository.CardRepository

class CardViewModel : ViewModel() {

    private val repo = CardRepository()
    val cards = repo.cards

    fun reloadCards() {
        repo.reloadCards()
    }

    fun swipedRight(position: Int) {
        val card = cards.value?.get(position)
    }

    fun swipedLeft(position: Int) {
        val card = cards.value?.get(position)
    }
}