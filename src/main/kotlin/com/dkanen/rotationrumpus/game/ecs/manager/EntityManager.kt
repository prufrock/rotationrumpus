package com.dkanen.rotationrumpus.game.ecs.manager

import com.dkanen.rotationrumpus.collections.graphs.Graph
import com.dkanen.rotationrumpus.collections.graphs.Scene
import com.dkanen.rotationrumpus.game.GameInput
import com.dkanen.rotationrumpus.game.Node
import com.dkanen.rotationrumpus.game.World
import com.dkanen.rotationrumpus.game.ecs.components.behaviors.buttons.TapButtonCfg
import com.dkanen.rotationrumpus.game.ecs.components.behaviors.buttons.ToggleButtonCfg
import com.dkanen.rotationrumpus.game.ecs.components.cameras.Camera
import com.dkanen.rotationrumpus.game.ecs.components.graphics.Graphics
import com.dkanen.rotationrumpus.game.ecs.components.physics.Collision
import com.dkanen.rotationrumpus.game.ecs.entities.Entity
import com.dkanen.rotationrumpus.game.ecs.entities.EntitySlug
import com.dkanen.rotationrumpus.math.Vector2O
import org.openrndr.color.ColorRGBa
import org.openrndr.math.Vector2

interface EntityManager {
    // perhaps a tree to make searching faster?
    val sceneGraph: Scene

    // Maybe a map if I need to switch cameras by their label?
    val cameras: List<Entity>

    fun createDecoration(slug: EntitySlug, graphicsComponent: Graphics): Entity

    fun createCamera(slug: EntitySlug, camera: Camera, graphicsComponent: Graphics): Entity

    /**
     * Props can be seen and bumped into.
     */
    fun createProp(
        slug: EntitySlug,
        position: Vector2,
        radius: Double,
        colorStroke: ColorRGBa = ColorRGBa.BLACK,
        colorFill: ColorRGBa = ColorRGBa.GREEN,
        collisionResponse: Boolean = false
    ): Entity

    /**
     * Find an entity by its ID.
     */
    fun find(slug: EntitySlug): Entity?
    fun finalizeTick()

    fun createAnimatedDecoration(slug: EntitySlug, position: Vector2 = Vector2O(), radius: Double = 0.5, colorStroke: ColorRGBa = ColorRGBa.BLACK, colorFill: ColorRGBa = ColorRGBa.WHITE): Entity

    fun createAnimatedProp(
        slug: EntitySlug,
        position: Vector2 = Vector2O(),
        radius: Double = 0.5,
        colorStroke: ColorRGBa = ColorRGBa.BLACK,
        colorFill: ColorRGBa = ColorRGBa.WHITE,
        direction: Vector2 = Vector2O(),
        speed: Double = 0.0
    ): Entity

    /**
     * Props can be seen and bumped into.
     */
    fun createProp(slug: EntitySlug, collision: Collision, graphicsComponent: Graphics): Entity
    fun createToggleButton(slug: EntitySlug, position: Vector2, radius: Double, toggleButtonCfg: ToggleButtonCfg): Entity
    fun createTapButton(slug: EntitySlug, position: Vector2, radius: Double, tapButtonCfg: TapButtonCfg): Entity
    fun createFromGraph(name: String, graph: Graph<Node<String>>, origin: Vector2 = Vector2O())

    // Entity Table
    fun update(world: World, gameInput: GameInput)

    // Collisions Table
    fun largestIntersectedEntity(entity: Entity): Entity?
    fun largestIntersection(thisCollision: Collision): Vector2?
}