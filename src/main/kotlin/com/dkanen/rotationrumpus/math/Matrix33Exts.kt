package com.dkanen.rotationrumpus.math

import org.openrndr.math.*
import kotlin.math.abs
import kotlin.math.asin
import kotlin.math.atan2

/**
 * left, right, top, and bottom are in world coordinates
 */
fun orthographic(left: Double, right: Double, top: Double, bottom: Double): Matrix33 = Matrix33.fromColumnVectors(c0 = Vector3(
    x = (2 / (right - left)),
    y = 0.0,
    z = 0.0
), c1 = Vector3(
    x = 0.0,
    y = (2 / (top - bottom)),
    z = 0.0
), c2 = Vector3(
    x = -((right + left) / (right - left)),
    y = -((top + bottom) / (top - bottom)),
    z = 1.0
)
)

/**
 * Determines if the matrix is orthogonal.
 * Needs to be a function that accepts a tolerance.
 */
val Matrix33.isOrthogonal: Boolean
    get() = (this * this.transposed) == Matrix33.IDENTITY

fun Matrix33.orthogonalize(): Matrix33 {
    val (c0, c1, c2) = columns()
    val c0p = c0.normalized
    val c1p = (c1 - (((c1.dot(c0p))/(c0p.dot(c0p))) * c0p)).normalized
    val c2p = c0p.cross(c1p).normalized
    return Matrix33.fromColumnVectors(c0p, c1p, c2p)
}

fun Matrix33.orthoganlizeNonbiased(k: Double): Matrix33 {
    val (c0, c1, c2) = columns()
    val ortho: MutableList<Vector3> = mutableListOf(c0, c1, c2)
    repeat(10) {
        val (c0p, c1p, c2p) = ortho
        ortho[0] = (c0p - k * ((c0p.dot(c1)) / (c1.dot(c1))) * c1 - k * ((c0p.dot(c2)) / (c2.dot(c2))) * c2).normalized
        ortho[1] = (c1p - k * ((c1p.dot(c0)) / (c0.dot(c0))) * c0 - k * ((c1p.dot(c2)) / (c2.dot(c2))) * c2).normalized
        ortho[2] = (c2p - k * ((c2p.dot(c0)) / (c0.dot(c0))) * c0 - k * ((c2p.dot(c1)) / (c1.dot(c1))) * c1).normalized
    }

    val (c0p, c1p, c2p) = ortho
    return Matrix33.fromColumnVectors(c0p, c1p, c2p).orthogonalize()
}

fun Matrix33.taylorSeriesNormalization(): Matrix33 {
    val (xOrth, yOrth, zOrth) = columns()

    val xNorm = 0.5*(3-xOrth.dot(xOrth))*xOrth
    val yNorm = 0.5*(3-yOrth.dot(yOrth))*yOrth
    val zNorm = 0.5*(3-zOrth.dot(zOrth))*zOrth

    return Matrix33.fromColumnVectors(xNorm, yNorm, zNorm)
}

/**
 * The name is comes from the reference:
 * https://varunagrawal.github.io/2020/02/11/fast-orthogonalization/
 */
fun Matrix33.renormalization(): Matrix33 {
    // I'm pretty sure I need to also compensate for drift into the z-axis.
    val (x, y, z) = columns()
    val e = x.dot(y)
    val xOrth = x - (0.5*e*y)
    val yOrth = y - (0.5*e*x)
    val zOrth = xOrth.cross(yOrth)

    // taylor-series based renormalization
    val xNorm = 0.5*(3-xOrth.dot(xOrth))*xOrth
    val yNorm = 0.5*(3-yOrth.dot(yOrth))*yOrth
    val zNorm = 0.5*(3-zOrth.dot(zOrth))*zOrth

    return Matrix33.fromColumnVectors(xNorm.normalized, yNorm.normalized, zNorm.normalized)
}

fun Matrix33.columns(): List<Vector3> {
    return listOf(Vector3(c0r0, c0r1, c0r2), Vector3(c1r0, c1r1, c1r2), Vector3(c2r0, c2r1, c2r2))
}

fun Matrix33.eq(other: Matrix33, atol: Double): Boolean {
    return (this - other).columns().all { it.length <= atol }
}

/**
 * https://gamemath.com/book/orient.html#converting_between_forms
 */
fun Matrix33.eulerAngles(): Triple<Double, Double, Double> {
    // We will compute the Euler angle values in radians
    // and store them here:
    val heading: Double
    val pitch: Double
    val bank: Double

    // Extract pitch from m32, being careful for domain errors with
    // asin().  We could have values slightly out of range due to
    // floating point arithmetic.
    val sp: Double = -c2r1
    if (sp <= -1.0f) {
        pitch = -1.570796 // -pi/2
    } else if (sp >= 1.0) {
        pitch = 1.570796 // pi/2
    } else {
        pitch = asin(sp)
    }

   // Check for the Gimbal lock case, giving a slight tolerance
   // for numerical imprecision
    if (abs(sp) > 0.9999) {

        // We are looking straight up or down.
        // Slam bank to zero and just set heading
        bank = 0.0
        heading = atan2(-c0r2, c0r0)
    } else {

        // Compute heading
        heading = atan2(c2r0, c2r2)

        // Compute bank
        bank = atan2(c0r1, c1r1)
    }

    return Triple(heading, pitch, bank)
}

/**
 * Create a translation matrix from the vector.
 *
 * [translation] The amount to translate
 */
fun Matrix33.Companion.translate(translation: Vector3): Matrix33 {
    return translate(translation.x, translation.y)
}

/**
 * Create a translation matrix.
 *
 * [x] translate direction
 * [y] translate direction
 */
fun Matrix33.Companion.translate(x: Double, y: Double): Matrix33 {
    return fromColumnVectors(
        Vector3.UNIT_X,
        Vector3.UNIT_Y,
        Vector3(x, y, 1.0)
    )
}

fun Matrix33.Companion.translate(translate: Vector2): Matrix33 = translate(translate.x, translate.y)

fun Matrix33.Companion.scale(x: Double, y: Double): Matrix33 = fromColumnVectors(Vector3(x, 0.0, 0.0), Vector3(0.0, y, 0.0), Vector3(0.0, 0.0, 1.0))

fun Matrix33.Companion.transform(fixedPoint: Vector2, transformation: Matrix33): Matrix33 = translate(fixedPoint) * transformation * translate(-fixedPoint)
