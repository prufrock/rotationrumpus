package com.dkanen.rotationrumpus.game.ecs.components.input

import com.dkanen.rotationrumpus.game.GameInput
import com.dkanen.rotationrumpus.game.World
import com.dkanen.rotationrumpus.game.ecs.components.Component
import com.dkanen.rotationrumpus.game.ecs.entities.Entity
import com.dkanen.rotationrumpus.game.ecs.entities.EntitySlug
import com.dkanen.rotationrumpus.game.ecs.messages.UpdatePosition

class Input(override val entitySlug: EntitySlug) : Component {
    override fun update(entity: Entity, world: World, input: GameInput) {
        if (input.inputTargets.containsKey(entitySlug)) {
            input.inputTargets[entitySlug]?.let {
                entity.receive(UpdatePosition(it.velocity, this))
            }
        }
    }
}