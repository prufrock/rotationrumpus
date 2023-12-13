package com.dkanen.rotationrumpus.game.ecs.messages

import com.dkanen.rotationrumpus.game.ecs.components.Component
import com.dkanen.rotationrumpus.game.ecs.entities.EntitySlug
import org.openrndr.math.Vector2

sealed interface Message {
}

data class CollidedWith(val entitySlug: EntitySlug): Message

data class CollidedSet(val collisions: Set<EntitySlug>): Message

data class UpdatePosition(val displacement: Vector2, val from: Component): Message

data class UpdateStart(val position: Vector2, val from: Component): Message

data class UpdateEnd(val position: Vector2, val from: Component): Message

object Clicked : Message
