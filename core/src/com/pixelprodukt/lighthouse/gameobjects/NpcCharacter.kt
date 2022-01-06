package com.pixelprodukt.lighthouse.gameobjects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.maps.tiled.TiledMap
import com.pixelprodukt.lighthouse.interfaces.Interactable
import com.pixelprodukt.lighthouse.system.AnimationController
import com.pixelprodukt.lighthouse.system.Body
import kotlin.random.Random

open abstract class NpcCharacter(
    name: String,
    animationController: AnimationController,
    private val isStationary: Boolean = false
) : Character(name, animationController), Interactable {

    override val sensor: Body = Body()
    override val listeners: MutableList<(Event: Any) -> Unit> = mutableListOf()

    private var state = 2
    private var movingTime: Float = 0f
    private var boundsWidth: Float? = null
    private var boundsHeight: Float? = null

    init {
        speed = 0.2f
    }

    override fun update() {
        if (!isStationary) {
            movingTime -= Gdx.app.graphics.deltaTime
            if (movingTime > 0) {
                when(state) {
                    1 -> body.velocity.y = 1f
                    2 -> body.velocity.y = -1f
                    3 -> body.velocity.set(0f, 0f)
                    4 -> body.velocity.x = -1f
                    5 -> body.velocity.x = 1f
                    6 -> body.velocity.set(0f, 0f)
                }
            } else {
                body.velocity.set(0f, 0f)
                state = Random.nextInt(1, 7)
                movingTime = Random.nextInt(2, 6).toFloat()
            }

            if (boundsWidth != null) {
                if (body.position.x < 0f) body.position.x = 0f
                if (body.position.x + body.size.x > boundsWidth!!) body.position.x = boundsWidth!! - body.size.x
            }
            if (boundsHeight != null) {
                if (body.position.y < 0f) body.position.y = 0f
                if (body.position.y + body.size.y > boundsHeight!!) body.position.y = boundsHeight!! - body.size.y
            }

            body.velocity.nor()
            body.position.x += body.velocity.x * speed
            body.position.y += body.velocity.y * speed
        }
        sensor.position.x = (body.position.x + (body.size.x / 2)) - sensor.size.x / 2
        sensor.position.y = (body.position.y + (body.size.y / 2)) - sensor.size.y / 2
        super.update()
    }

    fun setBoundaries(map: TiledMap) {
        boundsWidth = (map.properties["width"] as Int * map.properties["tilewidth"] as Int).toFloat()
        boundsHeight = (map.properties["height"] as Int * map.properties["tileheight"] as Int).toFloat()
    }
}