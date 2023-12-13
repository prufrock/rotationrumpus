package com.dkanen.rotationrumpus.game.ecs.components.graphics

import com.dkanen.rotationrumpus.game.Camera
import com.dkanen.rotationrumpus.game.actor.Model
import com.dkanen.rotationrumpus.game.ecs.components.Component
import com.dkanen.rotationrumpus.game.ecs.entities.Entity
import com.dkanen.rotationrumpus.math.translate
import com.dkanen.rotationrumpus.math.worldToNdc
import org.openrndr.color.ColorRGBa
import org.openrndr.math.Matrix33
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3

interface Graphics: Component {
    // Don't need a position because the model's vertices get transformed to this position.
    var position: Vector2 //hot
    // I might not need a radius either since this would be scale...but I might need it for OpenRNDR's box drawing for now...
    var radius: Double //hot

    var model: Model //hot

    var colorFill: ColorRGBa //hot
    var colorStroke: ColorRGBa //hot

    var strokeWeight: Double // hot

    var label: String //cold - not everything that is rendered needs a label

    var worldToScreen: Matrix33 //hot

    val modelToUpRightMatrix: Matrix33
    var uprightToWorldMatrix: Matrix33

    var start: Vector2 // cold only for lines
    var end: Vector2 // cold only for lines

    var parent: Entity? //cold - not everything has a parent but it needs to be checked every time so maybe hot?
}

fun Graphics.renderModel(): List<Vector3> = model.vertices.map {
    modelToUpRightMatrix * uprightToWorldMatrix * worldToScreen * it
}

fun Graphics.worldToNdc(camera: Camera): Vector2 = position.worldToNdc(camera)
