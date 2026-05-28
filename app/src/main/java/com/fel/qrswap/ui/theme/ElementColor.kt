package com.fel.qrswap.ui.theme

import androidx.compose.ui.graphics.Color
import com.fel.qrswap.data.Element

fun Element.toColor(): Color {
    return when(this) {
        Element.FIRE -> Color(0xffe81b10)
        Element.WATER -> Color(0xff1e1ed6)
        Element.EARTH -> Color(0xff613216)
        Element.AIR -> Color(0xff04bf10)
        Element.ICE -> Color(0xff7dffeb)
        Element.DARK -> Color(0xff000000)
    }
}