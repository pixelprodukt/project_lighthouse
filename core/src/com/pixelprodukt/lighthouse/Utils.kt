package com.pixelprodukt.lighthouse

import com.badlogic.gdx.Gdx
import com.google.gson.Gson
import com.pixelprodukt.lighthouse.gameobjects.Character
import com.pixelprodukt.lighthouse.map.GameMap
import com.pixelprodukt.lighthouse.system.Direction
import com.pixelprodukt.lighthouse.system.Vector2
import java.util.concurrent.ConcurrentLinkedQueue

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

fun getCharacterConfig(filename: String): CharacterConfig {
    val gson = Gson()
    val file = Gdx.files.internal("data/$filename")
    return gson.fromJson(file.readString(), CharacterConfig::class.java)
}

enum class BehaviourType {
    NONE,
    IDLE,
    WALK
}

data class UpdateState(val map: GameMap, val direction: Direction?, val behaviourType: BehaviourType)

data class Wall(var x: Float, var y: Float, val width: Float = GRIDSIZE, val height: Float = GRIDSIZE)

open class GameObjectConfig(val id: String, var x: Int = 0, var y: Int = 0, val spritesheet: String, val behaviourLoop: MutableList<BehaviourConfig> = mutableListOf())
class CharacterConfig(
    id: String,
    x: Int = 0,
    y: Int = 0,
    spritesheet: String,
    val isPlayerControlled: Boolean = false,
    behaviourLoop: MutableList<BehaviourConfig> = mutableListOf()
    ): GameObjectConfig(id, x, y, spritesheet, behaviourLoop)

object EventHandler {

    private val listeners = hashMapOf<String, ConcurrentLinkedQueue<EventListener>>()

    fun addEventListener(eventType: String, listener: EventListener) {
        if (listeners[eventType] == null) {
            listeners[eventType] = ConcurrentLinkedQueue(mutableListOf(listener))
        } else {
            listeners[eventType]?.add(listener)
            Gdx.app.log("EventHandler, addEventListener", listeners[eventType]?.size.toString())
        }
    }

    fun removeEventListener(eventType: String, listener: EventListener) {
        if (listeners[eventType] != null) {
            listeners[eventType]?.removeIf { lis -> lis == listener }
            Gdx.app.log("EventHandler, removeEventListener", "removed")
        }
    }

    fun dispatchEvent(eventType: String, objectId: String) {
        val listeners = listeners[eventType]
        val iterator = listeners?.iterator()
        if (iterator != null) {
            while (iterator.hasNext()) {
                val item = iterator.next()
                item.update(objectId)
            }
        }
    }
}

interface EventListener {
    fun update(objectId: String)
}

data class BehaviourConfig(val type: BehaviourType, val direction: Direction, val duration: Float? = 0.0f)

open class Behaviour {
    var isFinished: Boolean = false
    open fun update(delta: Float) {}
}

class WalkBehaviour(
    private val target: Character,
    private val direction: Direction,
    private val distance: Float,
    private val speed: Float
    ): Behaviour() {
    private var startX: Float = 0.0f
    private  var startY: Float = 0.0f
    private var endX: Float = 0.0f
    private  var endY: Float = 0.0f

    fun begin() {
        startX = target.x
        startY = target.y
        endX = if (direction == Direction.LEFT) startX - distance else startX + distance
        endY = if (direction == Direction.DOWN) startY - distance else startY + distance
    }

    override fun update(delta: Float) {
        if (!isFinished) {

            when (direction) {
                Direction.UP -> {
                    target.y += 1 * speed
                    if (target.y >= endY) {
                        target.y = endY
                        isFinished = true
                    }
                }
                Direction.DOWN -> {
                    target.y += -1 * speed
                    if (target.y <= endY) {
                        target.y = endY
                        isFinished = true
                    }
                }
                Direction.LEFT -> {
                    target.x += -1 * speed
                    if (target.x <= endX) {
                        target.x = endX
                        isFinished = true
                    }
                }
                Direction.RIGHT -> {
                    target.x += 1 * speed
                    if (target.x >= endX) {
                        target.x = endX
                        isFinished = true
                    }
                }
            }
        }
        if (isFinished && !target.isPlayerControlled) {
            target.behaviourLoopIndex++
        }
    }
}

class IdleBehaviour(val duration: Float): Behaviour() {
    private var timePassed: Float = 0.0f

    override fun update(delta: Float) {
        if (!isFinished) {
            timePassed += delta
            isFinished = timePassed >= duration
        }
    }
}