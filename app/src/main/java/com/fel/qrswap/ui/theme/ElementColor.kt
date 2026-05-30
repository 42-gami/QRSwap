package com.fel.qrswap.ui.theme

import androidx.compose.ui.graphics.Color
import com.fel.qrswap.data.Element

fun Element.toColor(): Color {
    return when(this) {
        Element.FIRE -> Color(0xFFFF5100)
        Element.WATER -> Color(0xFF1E68D6)
        Element.EARTH -> Color(0xff613216)
        Element.AIR -> Color(0xFF49FF00)
        Element.ICE -> Color(0xff7dffeb)
        Element.DARK -> Color(0xFF6E1594)
    }
}