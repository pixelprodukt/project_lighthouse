package com.pixelprodukt.lighthouse.gameobjects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.pixelprodukt.lighthouse.*
import com.pixelprodukt.lighthouse.interfaces.Renderable
import com.pixelprodukt.lighthouse.interfaces.Updatable
import com.pixelprodukt.lighthouse.map.GameMap
import com.pixelprodukt.lighthouse.system.Body
import com.pixelprodukt.lighthouse.system.Direction
import com.pixelprodukt.lighthouse.system.Transform
import kotlinx.coroutines.*
import ktx.async.KtxAsync

open class GameObject(val config: GameObjectConfig) : Renderable, Updatable, Comparable<GameObject> {

    protected lateinit var region: TextureRegion

    val id: String = config.id
    var x: Float = toGrid(config.x)
    var y: Float = toGrid(config.y)
    val width = GRIDSIZE
    val height = GRIDSIZE
    var isActive = true
    var isMounted = false
    var behaviourLoop: MutableList<Behaviour> = config.behaviourLoop
    var behaviourLoopIndex: Int = 0

    open fun mount(map: GameMap) {
        isMounted = true
        map.addWall(x, y)
        //GlobalScope.launch { doBehaviourEvent(map) }
    }

    fun doBehaviourEvent(map: GameMap) {

        if (behaviourLoop.size == 0) {
            return
        }
        //Gdx.app.log("doBehaviourEvent", id)
        //Gdx.app.log("doBehaviourEvent", behaviourLoop.toString())

        //KtxAsync.launch { behaviourEvent(map) }
        //KtxAsync.launch { Gdx.app.log("KtxAsync", "Hello World") }

        // GlobalScope.launch {  }
        behaviourLoopIndex++

        if (behaviourLoopIndex == behaviourLoop.size) {
            behaviourLoopIndex = 0
        }
        doBehaviourEvent(map)

        behaviourEvent(map)

    }

    fun behaviourEvent(map: GameMap) {
        val who = (map.gameObjects.find { obj -> obj.id == id }) as Character
        who.startBehaviour(UpdateState(null, map = map), behaviourLoop[behaviourLoopIndex])
        //delay(500L)
    }

    override fun update(state: UpdateState) {}

    override fun render(batch: SpriteBatch) {
        val originX: Float = width.div(2)
        val originY: Float = height.div(2)
        batch.draw(region, x, y, originX, originY, width, height, 1.0f, 1.0f, 0.0f)
    }

    override fun compareTo(other: GameObject): Int {
        val tempY = other.y
        val compareY = y
        return if (tempY < compareY) -1 else if (tempY > compareY) 1 else 0
    }
}