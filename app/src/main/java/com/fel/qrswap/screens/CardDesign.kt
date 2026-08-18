package com.fel.qrswap.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fel.qrswap.data.Card
import com.fel.qrswap.data.Element
import com.fel.qrswap.ui.CardPortraitView

private const val REFERENCE_WIDTH_DP = 200f

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CardItem(
    card: com.fel.qrswap.data.Card,
    modifier: Modifier = Modifier
) {
    val displayName = if (card.isSpell) "Spell - ${card.name}" else "La Creatura - ${card.name}"


    Box(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth()
            .aspectRatio(0.58f)
            .border(2.dp, Color.Black, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .padding(4.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            border = BorderStroke(2.dp, Color.Black),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF54423A))
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val s = maxWidth.value / REFERENCE_WIDTH_DP
                val portraitSize = (maxWidth.value - 16 * s).dp
                val portraitCorner = (8 * s).dp

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding((8 * s).dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = displayName, fontSize = (10 * s).sp, lineHeight = (18 * s).sp, color = Color.White)
                        Text(text = "${card.cost}", fontSize = (14 * s).sp, color = Color.Blue)
                    }

                    Spacer(modifier = Modifier.height((8 * s).dp))


                    Box(
                        modifier = Modifier
                            .size(portraitSize)
                            .border(2.dp, Color.Black, RoundedCornerShape(portraitCorner))
                            .clip(RoundedCornerShape(portraitCorner)),
                        contentAlignment = Alignment.Center
                    ) {
                        CardPortraitView(
                            portrait = card.portrait,
                            element = card.element,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height((8 * s).dp))


                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape((4 * s).dp))
                            .background(Color(0xFFA28F7C))
                            .padding(horizontal = (6 * s).dp, vertical = (4 * s).dp),
                        contentAlignment = Alignment.TopStart
                    ) {
                        Text(
                            text = card.description,
                            fontSize = (10 * s).sp,
                            lineHeight = (11 * s).sp,
                            maxLines = 4,
                            color = Color.Black
                        )
                    }

                    // STATS
                    if (!card.isSpell) {
                        Spacer(modifier = Modifier.height((8 * s).dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = (4 * s).dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("HP: ${card.hp}", fontSize = (12 * s).sp, color = Color.White)
                            Text("DMG: ${card.dmg}", fontSize = (12 * s).sp, color = Color.White)
                        }
                    }
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
        description = "A powerful dragon engulfed in flames that has terrorized the mountains for centuries.",
        element = Element.FIRE,
        isSpell = false,
        hp = 10,
        dmg = 8,
        cost = 5,
        portrait = ByteArray(128)
    )
    CardItem(sampleCard)
}