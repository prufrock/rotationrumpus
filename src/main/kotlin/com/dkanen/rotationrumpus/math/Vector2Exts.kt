package com.dkanen.rotationrumpus.math

import com.dkanen.rotationrumpus.game.Camera
import com.dkanen.rotationrumpus.game.ecs.components.cameras.Camera as EcsCamera
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3

fun Vector2.ndcToScreen(width: Double, height: Double, flipX: Boolean = false, flipY: Boolean = false): Vector2 {
    return Vector2(x = x.ndcToScreen(width, flipX), y = y.ndcToScreen(height, flip = flipY))
}

val Vector2.p: Vector3
    get() = Vector3(this.x, this.y, 1.0)

/**
 * left and right are in world coordinates
 */
fun Vector2.worldToNdc(left: Double, right: Double, top: Double, bottom: Double): Vector2 = this.p.worldToNdc(left, right, top, bottom)

fun Vector2.worldToNdc(camera: Camera): Vector2 = this.p.worldToNdc(camera)

fun Vector2.ndcToWorld(camera: Camera): Vector2 = (camera.project().inversed * this.p).xy
fun Vector2.ndcToWorld(camera: EcsCamera): Vector2 = (camera.worldToNdc.inversed * this.p).xy

fun Vector2.ndcToWorld(camera: EcsCamera, zoom: Double): Vector2 = (camera.viewingTransform(zoom).inversed * this.p).xy
fun Vector2O() = Vector2(0.0)
