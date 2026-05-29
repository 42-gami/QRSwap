package com.fel.qrswap.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fel.qrswap.data.CardViewModel

@Composable
fun CollectionScreen(viewModel: CardViewModel) {

    val cardsState = viewModel.allCards.collectAsState()
    val cards = cardsState.value

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize()
    ) {
        items(cards) { card ->
            CardItem(
                card,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}