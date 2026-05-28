package com.fel.qrswap.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CardViewModel(private val repository: CardRepository) : ViewModel() {


    init {
        deleteAll()
    }

    private fun insertTestCards() {
        viewModelScope.launch {
            repository.insert(
                Card(
                    name = "Fire Dragon",
                    description = "A test card",
                    isSpell = false,
                    hp = 10,
                    dmg = 8,
                    cost = 5,
                    portrait = ByteArray(0),
                    element = Element.FIRE
                )
            )
            repository.insert(
                Card(
                    name = "Ice Dragon",
                    description = "A test card",
                    isSpell = false,
                    hp = 10,
                    dmg = 8,
                    cost = 5,
                    portrait = ByteArray(0),
                    element = Element.ICE
                )
            )
            repository.insert(
                Card(
                    name = "Air Dragon",
                    description = "A test card",
                    isSpell = false,
                    hp = 10,
                    dmg = 8,
                    cost = 5,
                    portrait = ByteArray(0),
                    element = Element.AIR
                )
            )
            repository.insert(
                Card(
                    name = "Fireball",
                    description = "A test card",
                    isSpell = false,
                    portrait = ByteArray(0),
                    element = Element.FIRE,
                    cost = 1
                )
            )
            repository.insert(
                Card(
                    name = "Fireball",
                    description = "A test card",
                    isSpell = false,
                    portrait = ByteArray(0),
                    element = Element.FIRE,
                    cost = 1
                )
            )
            repository.insert(
                Card(
                    name = "Fireball",
                    description = "A test card",
                    isSpell = false,
                    portrait = ByteArray(0),
                    element = Element.FIRE,
                    cost = 1
                )
            )
            repository.insert(
                Card(
                    name = "Fireball",
                    description = "A test card",
                    isSpell = false,
                    portrait = ByteArray(0),
                    element = Element.FIRE,
                    cost = 1
                )
            )
        }
    }

    val allCards: StateFlow<List<Card>> = repository.allCards
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun insert(card: Card) = viewModelScope.launch {
        repository.insert(card)
    }

    fun delete(card: Card) = viewModelScope.launch {
        repository.delete(card)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}

class CardViewModelFactory(private val repository: CardRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
