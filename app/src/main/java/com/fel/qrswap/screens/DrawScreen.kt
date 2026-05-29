package com.fel.qrswap.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.fel.qrswap.data.Element
import com.fel.qrswap.ui.theme.toColor

private const val GRID_SIZE = 32


@Composable
fun DrawScreen(
    element: Element,
    pixels: Array<BooleanArray>,
    onPixelsChanged: (Array<BooleanArray>) -> Unit
) {




    var drawMode by remember {
        mutableStateOf(true)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Row {
            Button(
                onClick = {
                    drawMode = true
                }
            ) {
                Text("Paint")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    drawMode = false
                }
            ) {
                Text("Erase")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Canvas(
            modifier = Modifier
                .size(320.dp)
                .background(Color.LightGray)
                .pointerInput(drawMode) {

                    detectDragGestures(
                        onDragStart = { offset ->
                            paintPixel(
                                offset = offset,
                                pixels = pixels,
                                drawMode = drawMode,
                                updatePixels = onPixelsChanged,
                                canvasSize = size.width.toFloat()
                            )
                        }
                    ) { change, _ ->

                        paintPixel(
                            offset = change.position,
                            pixels = pixels,
                            drawMode = drawMode,
                            updatePixels = onPixelsChanged,
                            canvasSize = size.width.toFloat()
                        )
                    }
                }
                .pointerInput(drawMode) {
                    detectTapGestures { offset ->
                        paintPixel(
                            offset = offset,
                            pixels = pixels,
                            drawMode = drawMode,
                            updatePixels = onPixelsChanged,
                            canvasSize = size.width.toFloat()
                        )
                    }
                }
        ) {

            val pixelSize = size.width / GRID_SIZE

            for (y in 0 until GRID_SIZE) {
                for (x in 0 until GRID_SIZE) {

                    val pixelColor =
                        if (pixels[y][x]) {
                            element.toColor()
                        } else {
                            Color.White
                        }

                    drawRect(
                        color = pixelColor,
                        topLeft = Offset(
                            x * pixelSize,
                            y * pixelSize
                        ),
                        size = Size(pixelSize, pixelSize)
                    )

                    drawRect(
                        color = Color.Gray,
                        topLeft = Offset(
                            x * pixelSize,
                            y * pixelSize
                        ),
                        size = Size(pixelSize, pixelSize),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(1f)
                    )
                }
            }
        }

    }
}

private fun paintPixel(
    offset: Offset,
    pixels: Array<BooleanArray>,
    drawMode: Boolean,
    updatePixels: (Array<BooleanArray>) -> Unit,
    canvasSize: Float
) {

    val pixelSize = canvasSize / GRID_SIZE

    val x = (offset.x / pixelSize).toInt()
    val y = (offset.y / pixelSize).toInt()

    if (x !in 0 until GRID_SIZE || y !in 0 until GRID_SIZE) {
        return
    }

    pixels[y][x] = drawMode

    updatePixels(
        Array(GRID_SIZE) { y ->
            pixels[y].clone()
        }
    )
}