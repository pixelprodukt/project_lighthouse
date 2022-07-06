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

    //private var state = 2
    private var movingTime: Float = 0f
    private var boundsWidth: Float? = null
    private var boundsHeight: Float? = null

    init {
        speed = 1.0f
    }

    override fun update(state: UpdateState) {
        /*if (!isStationary) {
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
                if (body.x < 0f) body.x = 0f
                if (body.x + body.width > boundsWidth!!) body.x = boundsWidth!! - body.width
            }
            if (boundsHeight != null) {
                if (body.y < 0f) body.y = 0f
                if (body.y + body.height > boundsHeight!!) body.y = boundsHeight!! - body.height
            }

            body.velocity.normalized
            body.x += body.velocity.x * speed
            body.y += body.velocity.y * speed
        }
        sensor.x = (body.x + (body.width / 2)) - sensor.width / 2
        sensor.y = (body.y + (body.height / 2)) - sensor.height / 2*/
        super.update(state)
    }

    fun setBoundaries(map: TiledMap) {
        boundsWidth = (map.properties["width"] as Int * map.properties["tilewidth"] as Int).toFloat()
        boundsHeight = (map.properties["height"] as Int * map.properties["tileheight"] as Int).toFloat()
    }
}