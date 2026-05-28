package com.fel.qrswap.screens

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fel.qrswap.data.CardViewModel

@Composable
fun CollectionScreen(viewModel: CardViewModel) {

    val cardsState = viewModel.allCards.collectAsState()
    val cards = cardsState.value

    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(cards) { card ->
            CardItem(card)
        }
    }
}