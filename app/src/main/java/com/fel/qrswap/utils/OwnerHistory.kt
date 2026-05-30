package com.fel.qrswap.utils

import com.fel.qrswap.data.Countries

data class OwnerEntry(val initials: String, val flag: String)

object OwnerHistory {

    private const val ENTRY_SIZE = 4        // 3 bajty inicjal, 1 bajt kraj
    private const val MAX_OWNERS = 5
    private const val HISTORY_SIZE = ENTRY_SIZE * MAX_OWNERS // 20 bajtów

    fun createWithOwner(initials: String, countryIndex: Int): ByteArray {
        val history = ByteArray(HISTORY_SIZE)
        writeEntry(history, 0, initials, countryIndex)
        return history
    }

    // działa jak kolejka
    fun append(history: ByteArray, initials: String, countryIndex: Int): ByteArray {
        val result = ByteArray(HISTORY_SIZE)

        for (slot in 1 until MAX_OWNERS) { // przesuwa istniejące rekordy o jeden, dodaje nowy na początek
            val srcOffset = (slot - 1) * ENTRY_SIZE
            val dstOffset = slot * ENTRY_SIZE
            history.copyInto(result, dstOffset, srcOffset, srcOffset + ENTRY_SIZE)
        }

        writeEntry(result, 0, initials, countryIndex)
        return result
    }

    fun read(history: ByteArray): List<OwnerEntry> {
        val entries = mutableListOf<OwnerEntry>()
        for (slot in 0 until MAX_OWNERS) {
            val offset = slot * ENTRY_SIZE
            val initialsBytes = history.copyOfRange(offset, offset + 3)
            val countryIndex = history[offset + 3].toInt() and 0xFF

            // pomijamy zerowe bajty
            if (initialsBytes.all { it == 0.toByte() }) continue

            val initials = String(initialsBytes, Charsets.ISO_8859_1).trimEnd('\u0000')
            val flag = Countries.fromIndex(countryIndex).flag
            entries.add(OwnerEntry(initials, flag))
        }
        return entries
    }

    private fun writeEntry(history: ByteArray, slot: Int, initials: String, countryIndex: Int) {
        val offset = slot * ENTRY_SIZE
        val initialsBytes = initials.take(3).toByteArray(Charsets.ISO_8859_1)
        initialsBytes.copyInto(history, offset, 0, initialsBytes.size)
        history[offset + 3] = countryIndex.toByte()
    }
}