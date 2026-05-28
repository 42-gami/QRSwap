package com.fel.qrswap.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class Card(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val isSpell: Boolean,
    val hp: Int? = null,
    val dmg: Int? = null,
    val cost: Int,
    val element: Element,
    val portrait: ByteArray
)
