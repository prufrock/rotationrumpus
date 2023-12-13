package com.dkanen.rotationrumpus.game.ecs.components.cameras

import com.dkanen.rotationrumpus.game.GameInput
import com.dkanen.rotationrumpus.game.World
import com.dkanen.rotationrumpus.game.ecs.entities.Entity
import com.dkanen.rotationrumpus.game.ecs.entities.EntitySlug
import com.dkanen.rotationrumpus.game.ecs.messages.UpdatePosition
import com.dkanen.rotationrumpus.math.orthographic
import org.openrndr.math.Matrix33
import org.openrndr.math.Vector2
import org.openrndr.panel.elements.round

class OrthoCamera(
    override val entitySlug: EntitySlug,
) : Camera {

    private var left = 0.0
    private var right = 8.0
    private var top = 0.0
    private var bottom = 8.0

    override val width: Double
        get() = (right-left).round(2)

    override val height: Double
        get() = (bottom-top).round(2)

    override val worldToNdc: Matrix33
        get() = orthographic(left = left, right = right, top = top, bottom = bottom)
    override fun update(entity: Entity, world: World, input: GameInput) {
    }

    override fun translate(cameraVelocity: Vector2) {
        left += cameraVelocity.x
        right += cameraVelocity.x
        top += cameraVelocity.y
        bottom += cameraVelocity.y
    }

    override fun <T> receive(message: T) {
        when(message) {
            is UpdatePosition -> {
                translate(message.displacement)
            }
        }
    }
}