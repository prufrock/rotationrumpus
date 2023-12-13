package com.dkanen.rotationrumpus.game.ecs.components

import com.dkanen.rotationrumpus.game.GameInput
import com.dkanen.rotationrumpus.game.World
import com.dkanen.rotationrumpus.game.ecs.entities.Entity
import com.dkanen.rotationrumpus.game.ecs.entities.EntitySlug

/**
 * Component may just be a base type to identify anything that can be a component.
 */
interface Component {
    val entitySlug: EntitySlug

    fun update(entity: Entity, world: World, input: GameInput)

    fun <T>receive(message: T) {

    }

    fun tickReset() {

    }

    fun finalizeUpdate() {

    }
}