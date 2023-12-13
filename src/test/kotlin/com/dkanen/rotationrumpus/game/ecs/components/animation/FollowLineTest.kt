package com.dkanen.rotationrumpus.game.ecs.components.animation

import com.dkanen.rotationrumpus.game.GameInput
import com.dkanen.rotationrumpus.game.World
import com.dkanen.rotationrumpus.game.ecs.components.Component
import com.dkanen.rotationrumpus.game.ecs.entities.Entity
import com.dkanen.rotationrumpus.game.ecs.entities.EntitySlug
import com.dkanen.rotationrumpus.game.ecs.entities.eslug
import com.dkanen.rotationrumpus.game.ecs.messages.UpdatePosition
import com.dkanen.rotationrumpus.inputNoChange
import com.dkanen.rotationrumpus.inputTimeStep
import com.dkanen.rotationrumpus.math.Vector2O
import com.dkanen.rotationrumpus.smallTestWorld
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.openrndr.math.Vector2

class FollowLineTest {

    @Test
    fun update() {
        val entity = Entity(slug = "pew-pew".eslug)

        entity.components += FollowLine(
            entity.slug,
            speed = 0.1,
            direction = Vector2(1.0, 0.0)
        )

        val mockComponent = MockComponent(entity.slug, Vector2O())
        entity.components += mockComponent

        entity.update(smallTestWorld(), GameInput(clickedEntity = null, input = inputNoChange()))

        assertEquals(Vector2O(), mockComponent.currentPosition)

        entity.update(smallTestWorld(), GameInput(clickedEntity = null, input = inputTimeStep(1.0)))

        assertEquals(Vector2(0.1, 0.0), mockComponent.currentPosition)

        entity.update(smallTestWorld(), GameInput(clickedEntity = null, input = inputTimeStep(1.0)))

        assertEquals(Vector2(0.2, 0.0), mockComponent.currentPosition)
    }

    class MockComponent(override val entitySlug: EntitySlug, var currentPosition: Vector2): Component {
        override fun update(entity: Entity, world: World, input: GameInput) {
        }

        override fun <T> receive(message: T) {
            when(message) {
                is UpdatePosition -> {
                    currentPosition += message.displacement
                }
            }
        }
    }
}