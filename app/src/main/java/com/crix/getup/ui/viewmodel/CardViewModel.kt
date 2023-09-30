package com.crix.getup.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.crix.getup.R
import com.crix.getup.data.CardRepository
import com.crix.getup.data.model.Card

class CardViewModel : ViewModel() {

    private val repo = CardRepository()
    private val _cards = MutableLiveData<MutableList<Card>>(repo.loadCards())
    val cards: LiveData<MutableList<Card>> = _cards

    fun reloadCards() {
        _cards.value!!.addAll(repo.loadCards().shuffled().toMutableList())
    }
}