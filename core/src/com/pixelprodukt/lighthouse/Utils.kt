package com.pixelprodukt.lighthouse

import com.badlogic.gdx.Gdx
import com.google.gson.Gson
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
    IDLE,
    WALK
}

data class UpdateState(val direction: Direction?, val map: GameMap)
data class Behaviour(val type: BehaviourType, val direction: Direction?, val time: Long = 0L, var characterId: String? = null, val retry: Boolean = false)

open class GameObjectConfig(val id: String, var x: Int = 0, var y: Int = 0, val spritesheet: String, val behaviourLoop: MutableList<Behaviour> = mutableListOf())
class CharacterConfig(
    id: String,
    x: Int = 0,
    y: Int = 0,
    spritesheet: String,
    val isPlayerControlled: Boolean = false,
    behaviourLoop: MutableList<Behaviour> = mutableListOf()
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