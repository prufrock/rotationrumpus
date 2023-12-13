package com.dkanen.rotationrumpus.openrndr

import org.openrndr.draw.Drawer
import org.openrndr.math.Vector2

fun Drawer.drawSquareAt(center: Vector2, halfSize: Double = 20.0) {
    drawSquareAt(center.x, center.y, halfSize)
}

fun Drawer.drawSquareAt(x: Double, y: Double, halfSize: Double = 2.5) {
    rectangle(x - (halfSize / 2), y - (halfSize / 2), halfSize, halfSize)
}