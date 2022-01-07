package com.pixelprodukt.lighthouse.system

import kotlin.math.abs
import kotlin.math.hypot
import kotlin.math.sqrt

class Vector2(var x: Float, var y: Float) {
    val magnitude: Float get() = hypot(this.x, this.y)
    val normalized: Vector2
        get() {
            val imag = (1.0 / magnitude).toFloat()
            return Vector2(this.x * imag, this.y * imag)
        }

    fun set(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    fun set(other: Vector2) {
        this.x = other.x
        this.y = other.y
    }

    operator fun times(other: Vector2): Vector2 {
        this.x = this.x * other.x
        this.y= this.y * other.y
        return this
    }

    operator fun times(float: Float): Vector2 {
        this.x = this.x * float
        this.y= this.y * float
        return this
    }

    override fun toString(): String {
        return "(x: $x, y: $y)"
    }
}

class Body(
    var x: Float = 0f,
    var y: Float = 0f,
    var width: Float = 0f,
    var height: Float = 0f,
    var isActive: Boolean = true,
    var isStatic: Boolean = false,
    var isSensor: Boolean = false
) {
    val velocity: Vector2 = Vector2(0f, 0f)
    val offset: Vector2 = Vector2(0f, 0f)
    val center get() = Vector2(x + (width / 2), y + (height / 2))

    fun xy(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    fun size(width: Float, height: Float) {
        this.width = width
        this.height = height
    }
}

fun resolveCollision(b1: Body, b2: Body) {

    val seperationVector = testBodyAgainstBody(b1, b2)

    if (seperationVector != null) {
        solveCollision(b1, b2, seperationVector)
    }
}

fun testBodyAgainstBody(a: Body, b: Body): Vector2? {
    // distance between the bodies
    val distanceX = (a.x + a.offset.x + (a.width / 2)) - (b.x + b.offset.x + (b.width / 2))
    val distanceY = (a.y + a.offset.y + (a.height / 2)) - (b.y + b.offset.y + (b.height / 2))

    val adx = abs(distanceX)
    val ady = abs(distanceY)

    // sum of the extends
    val shw = (a.width / 2) + (b.width / 2)
    val shh = (a.height / 2) + (b.height / 2)

    if (adx >= shw || ady >= shh) {
        // no intersection
        return null
    } else {
        // intersection

        // shortest seperation
        var sx = shw - adx
        var sy = shh - ady

        // ignore longer axis
        if (sx < sy) {
            if (sx > 0) sy = 0f
        } else {
            if (sy > 0) sx = 0f
        }

        // correct sign
        if (distanceX < 0) sx = -sx
        if (distanceY < 0) sy = -sy

        return Vector2(sx, sy)
    }
}

fun solveCollision(a: Body, b: Body, sVec: Vector2) {
    // find the collision normal
    val d = sqrt((sVec.x * sVec.x).toDouble() + (sVec.y * sVec.y).toDouble())
    val nx = sVec.x / d
    val ny = sVec.y / d

    // relative velocity
    val vx = a.velocity.x - b.velocity.x
    val vy = a.velocity.y - b.velocity.y

    // penetration speed
    val ps = (vx * nx) + (vy * ny)

    // objects moving towards?
    if (ps <= 0) {
        a.x = a.x + sVec.x
        a.y = a.y + sVec.y
    }
}

fun intersect(b1: Body, b2: Body): Boolean {
    return (b1.x + b1.offset.x < b2.x + b2.offset.x + b2.width &&
            b1.x + b1.offset.x + b1.width > b2.x + b2.offset.x &&
            b1.y + b1.offset.y < b2.y + b2.height + b2.offset.y &&
            b1.y + b1.offset.y + b1.height > b2.y + b2.offset.y)
}

fun pointIntersectsWithBody(point: Vector2, b2: Body): Boolean {
    return (point.x < b2.x + b2.width &&
            point.x > b2.x &&
            point.y < b2.y + b2.height &&
            point.y > b2.y)
}