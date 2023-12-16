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
    fun `orthogonalize a rotation matrix`() {
        val o = Orientation33(Matrix44.rotateY(90.0).matrix33)
        assertTrue(o.objectToUpright().isOrthogonal)

        val t = Orientation33(Matrix44.rotateZ(2.4).matrix33)
        val oo = t.objectToUpright().renormalization()
        assertTrue(oo.isOrthogonal)
        assertTrue((oo * oo.transposed).eq(Matrix33.IDENTITY, atol = 1.0e-3))
    }
}