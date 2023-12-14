package com.dkanen.rotationrumpus.math

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Matrix33
import org.openrndr.math.Matrix44
import org.openrndr.math.Vector3
import org.openrndr.math.transforms.rotateY
import org.openrndr.math.transforms.rotateZ

class Orientation33Test {
    @Test
    fun `defaults to identity`() {
        val v = Vector3(1.0, 2.0, 3.0)
        val o = Orientation33()
        assertEquals(Vector3(1.0, 2.0, 3.0), o.objectToUpright() * v)
        assertEquals(Vector3(1.0, 2.0, 3.0), o.uprightToObject() * v)
    }

    @Test
    fun `rotate heading 90 degrees`() {
        val v = Vector3(0.0, 0.0, 1.0)
        val o = Orientation33(Matrix44.rotateY(90.0).matrix33)
        val ov = o.objectToUpright() * v
        assertEquals(1.0, ov.x, 0.0001)
        assertEquals(0.0, ov.y, 0.0001)
        assertEquals(0.0, ov.z, 0.0001)

        val vo = o.uprightToObject() * ov
        assertEquals(0.0, vo.x, 0.0001)
        assertEquals(0.0, vo.y, 0.0001)
        assertEquals(1.0, vo.z, 0.0001)
    }

    @Test
    fun `is not orthogonal`() {
        // create an orthogonal matrix
        val o = Matrix33.fromColumnVectors(
            Vector3(1.0, 2.0, 3.0),
            Vector3(4.0, 5.0, 6.0),
            Vector3(7.0, 8.0, 9.0)
        )

        assertFalse(o.isOrthogonal)
    }

    @Test
    fun `is orthogonal`() {
        val c1 = Vector3(1.0 / 3.0, 2.0 / 3.0, -2.0 / 3.0)
        val c2 = Vector3(-2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0)
        val c3 = Vector3(2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0)

        // create an orthogonal matrix
        val o = Matrix33.fromColumnVectors(
            c1,
            c2,
            c3
        )
        val ot = o.transposed

        assertEquals(0.0, c1.dot(c2))
        assertEquals(0.0, c2.dot(c3))
        assertTrue(ot.isOrthogonal)
    }

    @Test
    fun `orthogonalize a matrix nonbiased`() {
        val c1 = Vector3(1.0 / 3.0, 2.0 / 3.1, -2.0 / 3.0)
        val c2 = Vector3(-2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0)
        val c3 = Vector3(2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0)

        // create an orthogonal matrix
        val o = Matrix33.fromColumnVectors(
            c1,
            c2,
            c3
        )
        assertFalse(o.isOrthogonal)
        val t = o * o.transposed
        val oo = o.orthoganlizeNonbiased(0.25)
        assertTrue((oo * oo.transposed).eq(Matrix33.IDENTITY, atol = 1e-1))
    }

    @Test
    fun `orthogonalize a matrix imu`() {
        val c1 = Vector3(1.0 / 3.0, 2.0 / 3.1, -2.0 / 3.0)
        val c2 = Vector3(-2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0)
        val c3 = Vector3(2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0)

        // create an orthogonal matrix
        val o = Matrix33.fromColumnVectors(
            c1,
            c2,
            c3
        )
        assertFalse(o.isOrthogonal)
        val t = o * o.transposed
        val oo = o.taylorSeriesNormalization()
        assertTrue((oo * oo.transposed).eq(Matrix33.IDENTITY, atol = 0.1))
    }

    @Test
    fun `orthogonalize a matrix renormalization`() {
        val c1 = Vector3(1.0 / 3.0, 2.0 / 3.1, -2.0 / 3.0)
        val c2 = Vector3(-2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0)
        val c3 = Vector3(2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0)

        // create an orthogonal matrix
        val o = Matrix33.fromColumnVectors(
            c1,
            c2,
            c3
        )
        assertFalse(o.isOrthogonal)
        val t = o * o.transposed
        var oo = o.renormalization()

        assertTrue((oo * oo.transposed).eq(Matrix33.IDENTITY, atol = 1.0e-3))
    }

    @Test
    fun `orthogonalize a rotation matrix`() {
        val o = Orientation33(Matrix44.rotateY(90.0).matrix33)
        assertTrue(o.objectToUpright().isOrthogonal)

        val t = Orientation33(Matrix44.rotateZ(2.4).matrix33)
        val oo = t.objectToUpright().renormalization()
        assertTrue(oo.isOrthogonal)
        assertTrue((oo * oo.transposed).eq(Matrix33.IDENTITY, atol = 1.0e-3))
    }
}