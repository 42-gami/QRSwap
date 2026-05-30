package com.fel.qrswap.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fel.qrswap.data.CardViewModel
import com.fel.qrswap.qr.QrCodeImage
import com.fel.qrswap.utils.CardPacket
import com.fel.qrswap.utils.OwnerHistory

@Composable
fun CollectionScreen(viewModel: CardViewModel) {

    val cards by viewModel.allCards.collectAsState()

    var selectedCard by remember { mutableStateOf<com.fel.qrswap.data.Card?>(null) }
    var relinquishedCard by remember { mutableStateOf<com.fel.qrswap.data.Card?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize()
        ) {
            items(cards) { card ->
                CardItem(
                    card,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedCard = card }
                )
            }
        }

        if (selectedCard != null) {
            CardInspectOverlay(
                card = selectedCard!!,
                onClose = { selectedCard = null },
                onRelinquish = { card ->
                    viewModel.delete(card)
                    selectedCard = null
                    relinquishedCard = card
                }
            )
        }

        if (relinquishedCard != null) {
            QrOverlay(
                card = relinquishedCard!!,
                onDismiss = { relinquishedCard = null }
            )
        }
    }
}

@Composable
fun CardInspectOverlay(
    card: com.fel.qrswap.data.Card,
    onClose: () -> Unit,
    onRelinquish: (com.fel.qrswap.data.Card) -> Unit
) {
    var showConfirm by remember { mutableStateOf(false) }

    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            title = { Text("Relinquish card?") },
            text = { Text("This will permanently delete \"${card.name}\" from your collection.") },
            confirmButton = {
                Button(onClick = { onRelinquish(card) }) { Text("Confirm") }
            },
            dismissButton = {
                Button(onClick = { showConfirm = false }) { Text("Cancel") }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable { onClose() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { showConfirm = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Relinquish card")
            }

            Spacer(modifier = Modifier.height(16.dp))

            CardItem(
                card = card,
                modifier = Modifier.fillMaxWidth(0.72f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                val owners = remember(card.ownerHistory) { OwnerHistory.read(card.ownerHistory) }

                if (owners.isEmpty()) {
                    Text("No previous owners.", color = Color.White)
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Previous owners:")
                        owners.forEach { entry ->
                            Text(
                                text = "${entry.flag} ${entry.initials}",
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QrOverlay(
    card: com.fel.qrswap.data.Card,
    onDismiss: () -> Unit
) {
    val bytes = remember(card.id) { CardPacket.serialize(card) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.95f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Text("Scan to receive \"${card.name}\"", color = Color.White)
            Spacer(modifier = Modifier.height(24.dp))
            QrCodeImage(bytes = bytes, size = 280.dp)
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onDismiss) { Text("Done") }
        }
    }
}