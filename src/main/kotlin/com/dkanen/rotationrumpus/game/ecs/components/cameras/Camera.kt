package com.dkanen.rotationrumpus.game.ecs.components.cameras

import com.dkanen.rotationrumpus.game.ecs.components.Component
import com.dkanen.rotationrumpus.game.ecs.entities.Entity
import com.dkanen.rotationrumpus.math.scale
import com.dkanen.rotationrumpus.math.transform
import com.dkanen.rotationrumpus.math.translate
import org.openrndr.math.Matrix33
import org.openrndr.math.Vector2
import org.openrndr.math.times

interface Camera: Component {
    val worldToNdc: Matrix33

    val width: Double
    val height: Double
    fun translate(cameraVelocity: Vector2)

    fun viewingTransform(zoomFactor: Double): Matrix33 {
        val cameraPosition = Vector2((width / 2), height / 2)
        return worldToNdc * Matrix33.transform(cameraPosition, Matrix33.scale(zoomFactor, zoomFactor))
    }
}