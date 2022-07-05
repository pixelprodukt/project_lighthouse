package com.pixelprodukt.lighthouse.gameobjects

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.pixelprodukt.lighthouse.interfaces.Renderable
import com.pixelprodukt.lighthouse.interfaces.Updatable
import com.pixelprodukt.lighthouse.map.GameMap
import com.pixelprodukt.lighthouse.system.Body
import com.pixelprodukt.lighthouse.system.Direction
import com.pixelprodukt.lighthouse.system.Transform

open class GameObject() : Renderable, Updatable, Comparable<GameObject> {

    var x = 0
    var y = 0
    protected lateinit var region: TextureRegion
    var isActive = true

    val width get() = region.regionWidth
    val height get() = region.regionHeight

    override fun update(state: UpdateState) {}

    override fun render(batch: SpriteBatch) {

        val width = region.regionWidth.toFloat()
        val height = region.regionHeight.toFloat()
        val originX: Float = width.div(2)
        val originY: Float = height.div(2)

        batch.draw(region, x - originX, y - originY, originX, originY, width, height, 1.0f, 1.0f, 0.0f)
    }

    override fun compareTo(other: GameObject): Int {
        val tempY = other.y
        val compareY = y
        return if (tempY < compareY) -1 else if (tempY > compareY) 1 else 0
    }
}

enum class BehaviourType {
    IDLE,
    WALK
}

data class UpdateState(val direction: Direction?, val map: GameMap)
data class Behaviour(val type: BehaviourType, val direction: Direction?)