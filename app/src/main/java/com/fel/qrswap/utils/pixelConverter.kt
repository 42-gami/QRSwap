package com.fel.qrswap.utils

fun pixelsToByteArray(
    pixels: Array<BooleanArray>
): ByteArray {

    val width = pixels[0].size
    val height = pixels.size

    val totalPixels = width * height
    val byteCount = (totalPixels + 7) / 8

    val result = ByteArray(byteCount)

    var bitIndex = 0

    for (y in pixels.indices) {
        for (x in pixels[y].indices) {

            if (pixels[y][x]) {

                val byteIndex = bitIndex / 8
                val bitPosition = bitIndex % 8

                result[byteIndex] =
                    (result[byteIndex].toInt() or (1 shl bitPosition)).toByte()
            }

            bitIndex++
        }
    }

    return result
}

fun byteArrayToPixels(
    data: ByteArray,
    width: Int,
    height: Int
): Array<BooleanArray> {

    val pixels = Array(height) {
        BooleanArray(width)
    }

    var bitIndex = 0

    for (y in 0 until height) {
        for (x in 0 until width) {

            val byteIndex = bitIndex / 8
            val bitPosition = bitIndex % 8

            pixels[y][x] =
                ((data[byteIndex].toInt() shr bitPosition) and 1) == 1

            bitIndex++
        }
    }

    return pixels
}