package com.pixelprodukt.lighthouse.map

import com.badlogic.gdx.maps.tiled.TiledMap
import com.pixelprodukt.lighthouse.gameobjects.GameObject
import com.pixelprodukt.lighthouse.interfaces.Interactable
import com.pixelprodukt.lighthouse.nextPosition
import com.pixelprodukt.lighthouse.system.Body
import com.pixelprodukt.lighthouse.system.Direction

class GameMap(
    val name: String,
    val tiledMap: TiledMap,
    val gameObjects: MutableList<GameObject>,
    val interactables: MutableList<Interactable>,
    val walls: MutableList<GameObject>,
    val warpEntries: List<WarpEntry>,
    val warpExits: List<WarpExit>
) {
    val width: Int = tiledMap.properties["width"] as Int * tiledMap.properties["tilewidth"] as Int
    val height: Int = tiledMap.properties["height"] as Int * tiledMap.properties["tileheight"] as Int

    fun isSpaceTaken(currentX: Float, currentY: Float, direction: Direction): Boolean {
        val nextPosition = nextPosition(currentX, currentY, direction)
        return walls.find { wall -> wall.x == nextPosition.x && wall.y == nextPosition.y } != null
    }
}