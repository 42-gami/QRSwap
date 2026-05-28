package com.fel.qrswap

import android.app.Application
import com.fel.qrswap.data.AppDatabase
import com.fel.qrswap.data.CardRepository

class QRSwapApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { CardRepository(database.cardDao()) }
}
