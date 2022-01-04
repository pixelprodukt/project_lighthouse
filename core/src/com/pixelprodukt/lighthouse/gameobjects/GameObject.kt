package com.pixelprodukt.lighthouse.gameobjects

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.pixelprodukt.lighthouse.interfaces.Renderable
import com.pixelprodukt.lighthouse.interfaces.Updatable
import com.pixelprodukt.lighthouse.system.Body
import com.pixelprodukt.lighthouse.system.Transform

open class GameObject : Renderable, Updatable, Comparable<GameObject> {

    val body: Body = Body(Vector2(0f, 0f), Vector2(0f, 0f))
    val transform: Transform = Transform()
    protected lateinit var region: TextureRegion
    var isActive = true

    private fun syncTransformWithBody() {
        transform.position.x = body.position.x
        transform.position.y = body.position.y
    }

    override fun update() {
        syncTransformWithBody()
    }

    override fun render(batch: SpriteBatch) {

        val width = region.regionWidth.toFloat()
        val height = region.regionHeight.toFloat()
        val originX: Float = width.div(2)
        val originY: Float = height.div(2)
        val offsetX = transform.offset.x
        val offsetY = transform.offset.y
        val originOffsetX = transform.originOffset.x
        val originOffsetY = transform.originOffset.y

        batch.draw(
            region,
            transform.position.x - originX + offsetX,
            transform.position.y - originY + offsetY,
            originX + originOffsetX,
            originY + originOffsetY,
            width,
            height,
            transform.scale.x,
            transform.scale.y,
            transform.rotation
        )
    }

    override fun compareTo(other: GameObject): Int {
        val tempY = other.body.position.y
        val compareY = body.position.y
        return if (tempY < compareY) -1 else if (tempY > compareY) 1 else 0
    }
}