package com.dkanen.rotationrumpus.game.ecs.components.behaviors

import com.dkanen.rotationrumpus.game.GameInput
import com.dkanen.rotationrumpus.game.World
import com.dkanen.rotationrumpus.game.ecs.components.Component
import com.dkanen.rotationrumpus.game.ecs.entities.Entity
import com.dkanen.rotationrumpus.game.ecs.entities.EntitySlug
import com.dkanen.rotationrumpus.game.ecs.messages.UpdateEnd
import com.dkanen.rotationrumpus.game.ecs.messages.UpdateStart
import com.dkanen.rotationrumpus.math.Vector2O
import org.openrndr.math.Vector2

class Edge(
    override val entitySlug: EntitySlug,
    val sourceEntitySlug: EntitySlug,
    val destinationEntitySlug: EntitySlug,
    var sourcePosition: Vector2 = Vector2O(),
    var destinationPosition: Vector2 = Vector2O(),
    ) : Component {
    override fun update(entity: Entity, world: World, input: GameInput) {
        world.entityManager.find(sourceEntitySlug)?.let { e ->
            sourcePosition = e.graphics?.position ?: sourcePosition
            entity.receive(UpdateStart(sourcePosition, this))
        }

        world.entityManager.find(destinationEntitySlug)?.let { e ->
            destinationPosition = e.graphics?.position ?: destinationPosition
            entity.receive(UpdateEnd(destinationPosition, this))
        }
    }
}