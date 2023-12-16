package com.dkanen.rotationrumpus.math

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Matrix33
import org.openrndr.math.Matrix44
import org.openrndr.math.Vector3
import org.openrndr.math.asRadians
import org.openrndr.math.transforms.rotateX
import org.openrndr.math.transforms.rotateY
import org.openrndr.math.transforms.rotateZ

class Matrix33ExtsTests {

    @Test
    fun orthographic() {
        val left = 0.0
        val right = 5.0
        val top = 0.0
        val bottom = 5.0
        val o = orthographic(left = 0.0, right = 5.0, top = 0.0, bottom = 5.0)
        // 0,0 in world space lands in the upper left corner of NDC
        assertEquals(Vector3(-1.0, 1.0, 1.0), o * Vector3(x = 0.0, y = 0.0, z = 1.0))
        // the middle of world space is the middle of NDC
        assertEquals(Vector3(0.0, 0.0, 1.0), o * Vector3(x = ((right - left) / 2), y = ((bottom - top) / 2), z = 1.0))
        // the maximums of world space is the lower left corner of NDC
        assertEquals(Vector3(1.0, -1.0, 1.0), o * Vector3(x = right, y = bottom, z = 1.0))
    }

    @Test
    fun eulerAnglesHeading() {
        val m = Matrix44.rotateY(10.0).matrix33
        val (h, _, _) = m.eulerAngles()

        assertEquals(10.0.asRadians, h, 0.0001)
    }

    @Test
    fun eulerAnglesPitch() {
        val m = Matrix44.rotateX(10.0).matrix33
        val (_, p, _) = m.eulerAngles()

        assertEquals(10.0.asRadians, p, 0.0001)
    }

    @Test
    fun eulerAnglesBank() {
        val m = Matrix44.rotateZ(10.0).matrix33
        val (_, _, b) = m.eulerAngles()

        assertEquals(10.0.asRadians, b, 0.0001)
    }

    @Test
    fun `is not orthogonal`() {
        val o = Matrix33.fromColumnVectors(
            Vector3(1.0, 2.0, 3.0),
            Vector3(4.0, 5.0, 6.0),
            Vector3(7.0, 8.0, 9.0)
        )

        assertFalse(o.isOrthogonal())
    }

    @Test
    fun `is orthogonal`() {
        val c1 = Vector3(1.0 / 3.0, 2.0 / 3.0, -2.0 / 3.0)
        val c2 = Vector3(-2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0)
        val c3 = Vector3(2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0)

        val o = Matrix33.fromColumnVectors(
            c1,
            c2,
            c3
        )
        val ot = o.transposed

        assertEquals(0.0, c1.dot(c2))
        assertEquals(0.0, c2.dot(c3))
        assertTrue(ot.isOrthogonal())
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
        assertFalse(o.isOrthogonal())
        val t = o * o.transposed
        val oo = o.orthoganlizeNonbiased(0.25)
        assertTrue((oo * oo.transposed).isOrthogonal())
    }

    @Test
    fun `orthogonalize a matrix renormalization`() {
        val c1 = Vector3(1.0 / 3.0, 2.0 / 3.1, -2.0 / 3.0)
        val c2 = Vector3(-2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0)
        val c3 = Vector3(2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0)

        val o = Matrix33.fromColumnVectors(
            c1,
            c2,
            c3
        )
        assertFalse(o.isOrthogonal())
        val oo = o.renormalization()

        assertTrue((oo * oo.transposed).isOrthogonal())
    }
}