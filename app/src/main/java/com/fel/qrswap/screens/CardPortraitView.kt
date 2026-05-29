package com.fel.qrswap.ui

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.fel.qrswap.data.Element
import com.fel.qrswap.utils.byteArrayToPixels
import com.fel.qrswap.ui.theme.toColor


private const val SIZE = 32

@Composable
fun CardPortraitView(
    portrait: ByteArray,
    element: Element,
    modifier: Modifier = Modifier
) {

    val pixels = byteArrayToPixels(portrait, SIZE, SIZE)

    Canvas(modifier = modifier) {

        drawRect(androidx.compose.ui.graphics.Color.White)

        val pixelSize = size.width / SIZE

        for (y in 0 until SIZE) {
            for (x in 0 until SIZE) {

                if (!pixels[y][x]) continue

                drawRect(
                    color = element.toColor(),
                    topLeft = Offset(x * pixelSize, y * pixelSize),
                    size = Size(pixelSize, pixelSize)
                )
            }
        }
    }
}