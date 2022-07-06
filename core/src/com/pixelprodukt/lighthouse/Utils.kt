package com.pixelprodukt.lighthouse

import com.pixelprodukt.lighthouse.system.Direction
import com.pixelprodukt.lighthouse.system.Vector2

const val GRIDSIZE = 16.0f

fun toGrid(n: Int): Float {
    return n * GRIDSIZE
}

fun nextPosition(initialX: Float, initialY: Float, direction: Direction): Vector2 {
    var x = initialX
    var y = initialY
    val size = GRIDSIZE

    when (direction) {
        Direction.LEFT -> x -= size
        Direction.RIGHT -> x += size
        Direction.UP -> y += size
        Direction.DOWN -> y -= size
    }
    return Vector2(x, y)
}

