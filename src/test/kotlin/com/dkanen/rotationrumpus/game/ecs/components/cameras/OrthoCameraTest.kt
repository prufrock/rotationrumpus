package com.dkanen.rotationrumpus.game.ecs.components.cameras

import com.dkanen.rotationrumpus.game.ecs.entities.eslug
import com.dkanen.rotationrumpus.math.Vector3Point
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3

class OrthoCameraTest {

    @Test
    fun translate() {
        val camera = OrthoCamera("c1".eslug)

        camera.translate(Vector2(2.0))

        val result = camera.worldToNdc * Vector3Point(0.0, 0.0)
        assertEquals(-1.5, result.x)
        assertEquals(1.5, result.y)
    }
}