package com.pixelprodukt.lighthouse.gameobjects

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.pixelprodukt.lighthouse.*
import com.pixelprodukt.lighthouse.interfaces.Renderable
import com.pixelprodukt.lighthouse.interfaces.Updatable
import com.pixelprodukt.lighthouse.map.GameMap
import com.pixelprodukt.lighthouse.system.Direction

open class GameObject(val config: GameObjectConfig) : Updatable, Renderable, Comparable<GameObject> {

    protected var region: TextureRegion? = null

    var x = toGrid(config.x)
    var y = toGrid(config.y)
    val width = GRIDSIZE
    val height = GRIDSIZE

    val wall: Wall = Wall(toGrid(config.x), toGrid(config.y))

    val id: String = config.id
    var direction: Direction? = Direction.DOWN

    override fun render(batch: SpriteBatch) {
        val originX: Float = width.div(2)
        val originY: Float = height.div(2)
        batch.draw(region, x, y, originX, originY, width, height, 1.0f, 1.0f, 0.0f)
    }

    override fun update(delta: Float, state: UpdateState) {}

    override fun compareTo(other: GameObject): Int {
        val tempY = other.y
        val compareY = y
        return if (tempY < compareY) -1 else if (tempY > compareY) 1 else 0
    }
}