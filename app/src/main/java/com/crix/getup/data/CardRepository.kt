package com.crix.getup.data

import com.crix.getup.R
import com.crix.getup.data.model.Card
import kotlin.random.Random

class CardRepository {

    fun loadCards(): MutableList<Card> {
        val list = mutableListOf<Card>()
        repeat(2) {
            list.add(
                randomCard()
            )
        }
        return list
    }

    private fun randomCard(): Card {
        val random = Random
        val image = when (random.nextInt(3)) {
            0 -> R.drawable.g1
            1 -> R.drawable.g2
            2 -> R.drawable.g3
            else -> R.drawable.ic_dashboard_black_24dp
        }
        var randomString = ""
        repeat(10) { randomString += random.nextInt(9) }
        return Card(randomString, image)
    }
}