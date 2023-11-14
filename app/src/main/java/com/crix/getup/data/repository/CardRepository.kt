package com.crix.getup.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crix.getup.R
import com.crix.getup.data.model.Card
import com.crix.getup.util.Auth
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlin.random.Random

val TAG = "CardRepository"

class CardRepository {

    private val db = Firebase.firestore
    private val auth = Auth
    private val cardCollection = "cards"

    private val _cards = MutableLiveData<MutableList<Card>>(loadCards())
    val cards: LiveData<MutableList<Card>> = _cards

    fun addCard(card: Card) {
        if(auth.currentAppUser.value != null) {
            db.collection(cardCollection)
                .document(card.uuid)
                .set(card::class.java)
        } else {
            Log.e(TAG, "addCard: No current app user logged in.")
        }
    }

    fun getCards() {
        if(auth.currentAppUser.value != null) {
            db.collection(cardCollection)
                .get()
                .addOnSuccessListener {
                    _cards.value = it.toObjects(Card::class.java)
                }
                .addOnFailureListener {
                    Log.e(TAG, "getCards: No cards in database.")
                }
        } else {
            Log.e(TAG, "getCards: No current app user logged in.")
        }
    }

    fun reloadCards() {
        _cards.value = loadCards().shuffled().toMutableList()
    }

    fun loadCards(): MutableList<Card> {
        val list = mutableListOf<Card>()
        repeat(20) {
            list.add(
                randomCard()
            )
        }
        return list
    }

    private fun randomCard(): Card {
        val random = Random
        val image = when (random.nextInt(10)) {
            0 -> R.drawable.g1
            1 -> R.drawable.g2
            2 -> R.drawable.g3
            3 -> R.drawable.g4
            4 -> R.drawable.g5
            5 -> R.drawable.g6
            6 -> R.drawable.g7
            7 -> R.drawable.g8
            8 -> R.drawable.g9
            9 -> R.drawable.g10
            else -> R.drawable.ic_dashboard_black_24dp
        }
        var randomString = ""
        repeat(10) { randomString += random.nextInt(9) }
        return Card(randomString, randomString.reversed(), image)
    }
}