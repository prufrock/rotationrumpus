package com.dkanen.rotationrumpus.math

import org.openrndr.math.Matrix33

/**
 * The class Orientation33 is a container for the matrices that describe the orientation of an object.
 * The matrices are always orthogonal, so the inverse is the transpose.
 */
data class Orientation33(private var destination: Matrix33 = Matrix33.IDENTITY) {
    fun objectToUpright():Matrix33 {
        return destination
    }

    fun uprightToObject(): Matrix33 {
        return destination.transposed
    }
}