package com.dkanen.rotationrumpus.game.ecs.components.behaviors

import com.dkanen.rotationrumpus.game.GameInput
import com.dkanen.rotationrumpus.game.World
import com.dkanen.rotationrumpus.game.ecs.entities.Entity
import com.dkanen.rotationrumpus.game.ecs.entities.EntitySlug

class Message(override val entitySlug: EntitySlug, val source: EntitySlug, val destination: EntitySlug, val type: Type = Type.Request) : Behavior {
    override fun update(entity: Entity, world: World, input: GameInput) {
    }

    enum class Type {
        Request,
        Response
    }
}