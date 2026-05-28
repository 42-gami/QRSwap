package com.fel.qrswap.ui.theme

import androidx.compose.ui.graphics.Color
import com.fel.qrswap.data.Element

fun Element.toColor(): Color {
    return when(this) {
        Element.FIRE -> Color.Red
        Element.WATER -> Color.Blue
        Element.EARTH -> Color(0x854000ff)
        Element.AIR -> Color.Green
        Element.ICE -> Color.Cyan
        Element.DARK -> Color.Black
    }
}