package com.fel.qrswap.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fel.qrswap.data.Card
import com.fel.qrswap.data.Element

@Composable
fun CardItem(card: Card) {

    val displayName = if (card.isSpell)
        "Spell - ${card.name}"
    else
        "Creature - ${card.name}"

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .aspectRatio(0.7f), // trading card proportions

        border = BorderStroke(2.dp, Color.Black),
        shape = RoundedCornerShape(12.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),

                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = displayName,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "${card.cost}",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .border(1.dp, Color.Black),

                contentAlignment = Alignment.Center
            ) {

                Text("IMAGE")
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
                    .padding(vertical = 4.dp)
            ) {

                Text(
                    text = card.description,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (!card.isSpell) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),

                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text("HP: ${card.hp}")

                    Text("DMG: ${card.dmg}")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardItemPreview() {

    val sampleCard = Card(
        id = 1,
        name = "Fire Dragon",
        description = "A powerful dragon engulfed in flames.",
        element = Element.FIRE,
        isSpell = true,
        hp = 10,
        dmg = 8,
        cost = 5,
        portrait = ByteArray(128)
    )

    CardItem(sampleCard)
}