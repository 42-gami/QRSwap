package com.fel.qrswap.utils

import com.fel.qrswap.data.Card
import com.fel.qrswap.data.Element
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream

object CardPacket {

    private val PREFIX = byteArrayOf(0x5A.toByte(), 0xDE.toByte())

    private const val MAX_NAME_LEN = 20
    private const val MAX_DESC_LEN = 100
    private const val PORTRAIT_BYTES = 128
    private const val OWNER_HISTORY_BYTES = 20

    fun serialize(card: Card): ByteArray {
        require(card.name.length <= MAX_NAME_LEN) { "Name exceeds $MAX_NAME_LEN characters" }
        require(card.description.length <= MAX_DESC_LEN) { "Description exceeds $MAX_DESC_LEN characters" }
        require(card.portrait.size == PORTRAIT_BYTES) { "Portrait must be $PORTRAIT_BYTES bytes (32x32 1bpp)" }

        val buf = ByteArrayOutputStream()
        val out = DataOutputStream(buf)

        //zapisywanie danych

        out.write(PREFIX)

        // bit 0 - isSpell, bity 1-3 element, pozostałe nieużywane
        val flags = (if (card.isSpell) 0x01 else 0x00) or
                ((card.element.ordinal and 0x07) shl 1)
        out.writeByte(flags)

        // Name 1 bajt - długość, pozostałe dane
        val nameBytes = card.name.toByteArray(Charsets.UTF_8)
        out.writeByte(nameBytes.size)
        out.write(nameBytes)

        // Description 1 bajt - długość
        val descBytes = card.description.toByteArray(Charsets.UTF_8)
        out.writeByte(descBytes.size)
        out.write(descBytes)

        out.write(card.portrait)

        out.writeByte(card.hp ?: 0)

        out.writeByte(card.dmg ?: 0)

        out.writeByte(card.cost)

        out.write(card.ownerHistory)

        out.flush()
        return buf.toByteArray()
    }

    fun deserialize(packet: ByteArray): Card {
        var i = 0

        // Prefix
        require(packet.size >= 2 &&
                packet[0] == 0x5A.toByte() &&
                packet[1] == 0xDE.toByte()) { "Invalid prefix" }
        i += 2

        // Flags
        val flags   = packet[i++].toInt() and 0xFF
        val isSpell = (flags and 0x01) != 0
        val element = Element.entries[(flags shr 1) and 0x07]

        // Name
        val nameLen = packet[i++].toInt() and 0xFF
        val name = String(packet, i, nameLen, Charsets.UTF_8)
        i += nameLen

        // Description
        val descLen = packet[i++].toInt() and 0xFF
        val description = String(packet, i, descLen, Charsets.UTF_8)
        i += descLen

        // Portrait
        val portrait = packet.copyOfRange(i, i + PORTRAIT_BYTES)
        i += PORTRAIT_BYTES

        // hp
        val hp  = (packet[i++].toInt() and 0xFF).takeUnless { isSpell }

        // dmg
        val dmg = (packet[i++].toInt() and 0xFF).takeUnless { isSpell }

        // Cost
        val cost = packet[i++].toInt() and 0xFF

        // Owner history
        val ownerHistory = packet.copyOfRange(i, i + OWNER_HISTORY_BYTES)
        i += OWNER_HISTORY_BYTES

        return Card(
            name = name,
            description = description,
            isSpell = isSpell,
            hp = hp,
            dmg = dmg,
            cost = cost,
            element = element,
            portrait = portrait,
            ownerHistory = ownerHistory
        )
    }

    fun prefixCheck(packet: ByteArray): Boolean =
        packet.size >= 2 &&
                packet[0] == 0x5A.toByte() &&
                packet[1] == 0xDE.toByte()
}