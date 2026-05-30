package com.fel.qrswap.qr

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun QrCodeImage(
    bytes: ByteArray,
    size: Dp = 256.dp,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val px = with(density) { size.roundToPx() }

    val bitmap: Bitmap = remember(bytes) {
        generate(bytes, px)
    }

    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = "QR Code",
        modifier = modifier
    )
}