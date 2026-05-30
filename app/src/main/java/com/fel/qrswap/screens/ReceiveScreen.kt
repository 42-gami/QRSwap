package com.fel.qrswap.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.fel.qrswap.data.Card
import com.fel.qrswap.utils.CardPacket
import com.fel.qrswap.data.CardViewModel
import com.fel.qrswap.data.Countries
import com.fel.qrswap.data.UserProfile
import com.fel.qrswap.utils.OwnerHistory

@Composable
fun ReceiveScreen(viewModel: CardViewModel) {

    var message by remember { mutableStateOf<String?>(null) }

    val scanner = rememberLauncherForActivityResult(ScanContract()) { result ->
        if (result.contents == null) {
            message = "Scan cancelled or failed."
            return@rememberLauncherForActivityResult
        }

        val bytes = result.contents.toByteArray(Charsets.ISO_8859_1)


        android.util.Log.d("QRSwap", "bytes size: ${bytes.size}")
        android.util.Log.d("QRSwap", "byte[0]: ${bytes[0].toInt() and 0xFF} (expected ${0x5A})")
        android.util.Log.d("QRSwap", "byte[1]: ${bytes[1].toInt() and 0xFF} (expected ${0xDE})")

        if (!CardPacket.prefixCheck(bytes)) {
            message = "Not a valid QRSwap card."
            return@rememberLauncherForActivityResult
        }

        val card = runCatching { CardPacket.deserialize(bytes) }.getOrElse {
            message = "Card data was corrupted."
            return@rememberLauncherForActivityResult
        }

        val updatedHistory = OwnerHistory.append(
            card.ownerHistory,
            UserProfile.initials,
            Countries.toIndex(UserProfile.country)
        )

        viewModel.insert(card.copy(ownerHistory = updatedHistory))
        message = "\"${card.name}\" added to your collection!"
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            scanner.launch(ScanOptions().apply {
                setBeepEnabled(false)
                setOrientationLocked(true)
                setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            })
        }) {
            Text("Scan Card")
        }

        message?.let {
            Spacer(modifier = Modifier.height(24.dp))
            Text(it)
        }
    }
}