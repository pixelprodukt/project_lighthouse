package com.pixelprodukt.lighthouse.map

import com.badlogic.gdx.maps.tiled.TiledMap
import com.pixelprodukt.lighthouse.Wall
import com.pixelprodukt.lighthouse.gameobjects.GameObject
import com.pixelprodukt.lighthouse.interfaces.Interactable
import com.pixelprodukt.lighthouse.nextPosition
import com.pixelprodukt.lighthouse.system.Direction

class GameMap(
    val name: String,
    val tiledMap: TiledMap,
    val gameObjects: MutableList<GameObject>,
    val interactables: MutableList<Interactable>,
    val walls: MutableList<Wall>,
    val warpEntries: List<WarpEntry>,
    val warpExits: List<WarpExit>
) {
    val width: Int = tiledMap.properties["width"] as Int * tiledMap.properties["tilewidth"] as Int
    val height: Int = tiledMap.properties["height"] as Int * tiledMap.properties["tileheight"] as Int

    var isCutscenePlaying = false

    fun isSpaceTaken(currentX: Float, currentY: Float, direction: Direction): Boolean {
        val nextPosition = nextPosition(currentX, currentY, direction)
        return walls.find { wall -> wall.x == nextPosition.x && wall.y == nextPosition.y } != null
    }

    fun addWall(initX: Float, initY: Float) {
        walls.add(Wall(initX, initY))
    }

    fun removeWall(x: Float, y: Float) {
        walls.removeIf { wall -> wall.x == x && wall.y == y }
    }

    fun moveWall(wasX: Float, wasY: Float, direction: Direction) {
        removeWall(wasX, wasY)
        val nextPosition = nextPosition(wasX, wasY, direction)
        addWall(nextPosition.x, nextPosition.y)
    }
}