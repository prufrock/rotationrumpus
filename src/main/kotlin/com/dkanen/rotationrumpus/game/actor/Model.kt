package com.dkanen.rotationrumpus.game.actor

import com.dkanen.rotationrumpus.game.Game
import com.dkanen.rotationrumpus.game.ecs.components.graphics.Graphics
import com.dkanen.rotationrumpus.game.ecs.components.graphics.renderModel
import com.dkanen.rotationrumpus.math.ndcToScreen
import com.dkanen.rotationrumpus.math.p
import com.dkanen.rotationrumpus.openrndr.FontBook
import com.dkanen.rotationrumpus.openrndr.drawSquareAt
import org.openrndr.draw.Drawer
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3

open class Model {
    //TODO: use a companion object so you don't have to make new models for every model.
    open val vertices: List<Vector3> = listOf()

    open fun draw(drawer: Drawer, graphics: Graphics, game: Game, width: Double, height: Double, fontBook: FontBook) {

    }

    open fun draw(drawer: Drawer, graphics: Graphics, game: Game, width: Double, height: Double, zoom: Double, fontBook: FontBook) {

    }
}

class Point: Model() {
    override val vertices: List<Vector3> = listOf(Vector3(0.0, 0.0, 1.0))
}

class Square: Model() {
    override val vertices: List<Vector3> = listOf(Vector3(0.0, 0.0, 1.0))
    override fun draw(drawer: Drawer, graphics: Graphics, game: Game, width: Double, height: Double, fontBook: FontBook) {
        drawer.fill = graphics.colorFill
        drawer.stroke = graphics.colorStroke

        val camera = game.world.eCamera.camera!!
        val location = (camera.worldToNdc * graphics.renderModel().first()).xy
            .ndcToScreen(width = width, height = height, flipY = true)
        // Should the boxes relate to the width of the camera or the width of the world?
        // If I use the camera width then I get zoom by scaling the camera width and height!
        // Also, divide by 2.0 because the size of the box is specified as the `halfSize`.
        val halfSize = graphics.radius * (width / (camera.width / 2))
        drawer.drawSquareAt(location, halfSize)
    }

    override fun draw(drawer: Drawer, graphics: Graphics, game: Game, width: Double, height: Double, zoom: Double, fontBook: FontBook) {
        drawer.fill = graphics.colorFill
        drawer.stroke = graphics.colorStroke

        val camera = game.world.eCamera.camera!!
        val location = (camera.viewingTransform(zoom) * graphics.renderModel().first()).xy
            .ndcToScreen(width = width, height = height, flipY = true)
        // Should the boxes relate to the width of the camera or the width of the world?
        // If I use the camera width then I get zoom by scaling the camera width and height!
        // Also, divide by 2.0 because the size of the box is specified as the `halfSize`.
        val halfSize = graphics.radius * (width * zoom / (camera.width / 2))
        drawer.drawSquareAt(location, halfSize)
    }
}

class Label: Model() {
    override val vertices: List<Vector3> = listOf(Vector3(0.0, 0.0, 1.0))
    override fun draw(drawer: Drawer, graphics: Graphics, game: Game, width: Double, height: Double, fontBook: FontBook) {
        drawer.fill = graphics.colorFill
        drawer.stroke = graphics.colorStroke

        val camera = game.world.eCamera.camera!!
        val location = (camera.worldToNdc * graphics.renderModel().first()).xy
            .ndcToScreen(width = width, height = height, flipY = true)
        // Should the boxes relate to the width of the camera or the width of the world?
        // If I use the camera width then I get zoom by scaling the camera width and height!
        // Also, divide by 2.0 because the size of the box is specified as the `halfSize`.
        val halfSize = graphics.radius * (width / (camera.width / 2))

        drawer.fontMap = fontBook.medium

        val labelWidth = graphics.label.fold(0.0) { acc, c ->
            acc + fontBook.medium.characterWidth(c)
        }

        drawer.text(graphics.label, location.x - (labelWidth / 3), location.y)
    }

    override fun draw(drawer: Drawer, graphics: Graphics, game: Game, width: Double, height: Double, zoom: Double, fontBook: FontBook) {
        drawer.fill = graphics.colorFill
        drawer.stroke = graphics.colorStroke

        val camera = game.world.eCamera.camera!!

        val location = (camera.viewingTransform(zoom) * graphics.renderModel().first()).xy
            .ndcToScreen(width = width, height = height, flipY = true)
        // Should the boxes relate to the width of the camera or the width of the world?
        // If I use the camera width then I get zoom by scaling the camera width and height!
        // Also, divide by 2.0 because the size of the box is specified as the `halfSize`.
        val halfSize = graphics.radius * (width / (camera.width / 2))

        val font = when(zoom) {
            in 0.0..0.8 -> fontBook.small
            in 0.8..1.3 -> fontBook.medium
            in 1.3..2.0 -> fontBook.large
            else -> fontBook.large}

        drawer.fontMap = font

        val labelWidth = graphics.label.fold(0.0) { acc, c ->
            acc + font.characterWidth(c)
        }

        drawer.text(graphics.label, location.x - (labelWidth / 3), location.y)
    }
}

class Line: Model() {
    override fun draw(drawer: Drawer, graphics: Graphics, game: Game, width: Double, height: Double, fontBook: FontBook) {
        drawer.fill = graphics.colorFill
        drawer.stroke = graphics.colorStroke
        drawer.strokeWeight = graphics.strokeWeight

        val camera = game.world.eCamera.camera!!
        val locationStart = (camera.worldToNdc * graphics.start.p).xy
            .ndcToScreen(width = width, height = height, flipY = true)
        // Should the boxes relate to the width of the camera or the width of the world?
        // If I use the camera width then I get zoom by scaling the camera width and height!
        // Also, divide by 2.0 because the size of the box is specified as the `halfSize`.
        val halfSize = graphics.radius * (width / (camera.width / 2))

        val locationDest = (camera.worldToNdc * graphics.end.p).xy
            .ndcToScreen(width = width, height = height, flipY = true)
        // Should the boxes relate to the width of the camera or the width of the world?
        // If I use the camera width then I get zoom by scaling the camera width and height!
        // Also, divide by 2.0 because the size of the box is specified as the `halfSize`.
        drawer.lineSegment(start = locationStart, end = locationDest)
    }

    override fun draw(drawer: Drawer, graphics: Graphics, game: Game, width: Double, height: Double, zoom: Double, fontBook: FontBook) {
        drawer.fill = graphics.colorFill
        drawer.stroke = graphics.colorStroke
        drawer.strokeWeight = graphics.strokeWeight

        val camera = game.world.eCamera.camera!!
        // Zoom to the center of the camera.
        val cameraPosition = Vector2(-1 * (camera.width / 2), camera.height / 2)
        val locationStart = (camera.viewingTransform(zoom) * graphics.start.p).xy
            .ndcToScreen(width = width, height = height, flipY = true)
        // Should the boxes relate to the width of the camera or the width of the world?
        // If I use the camera width then I get zoom by scaling the camera width and height!
        // Also, divide by 2.0 because the size of the box is specified as the `halfSize`.
        val halfSize = graphics.radius * (width / (camera.width / 2))

        val locationDest = (camera.viewingTransform(zoom) *  graphics.end.p).xy
            .ndcToScreen(width = width, height = height, flipY = true)
        // Should the boxes relate to the width of the camera or the width of the world?
        // If I use the camera width then I get zoom by scaling the camera width and height!
        // Also, divide by 2.0 because the size of the box is specified as the `halfSize`.
        drawer.lineSegment(start = locationStart, end = locationDest)
    }
}