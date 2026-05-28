package com.fel.qrswap.data

import kotlinx.coroutines.flow.Flow

class CardRepository(private val cardDao: CardDao) {
    val allCards: Flow<List<Card>> = cardDao.getAllCards()

    suspend fun insert(card: Card) {
        cardDao.insertCard(card)
    }

    suspend fun delete(card: Card) {
        cardDao.deleteCard(card)
    }

    suspend fun getById(id: Int): Card? {
        return cardDao.getCardById(id)
    }

    suspend fun deleteAll() {
        cardDao.deleteAll()
    }
}
