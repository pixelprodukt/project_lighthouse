package com.pixelprodukt.lighthouse.map

import com.badlogic.gdx.maps.tiled.TiledMap
import com.pixelprodukt.lighthouse.gameobjects.GameObject
import com.pixelprodukt.lighthouse.interfaces.Interactable
import com.pixelprodukt.lighthouse.system.Body

class GameMap(
    val name: String,
    val tiledMap: TiledMap,
    val gameObjects: MutableList<GameObject>,
    val interactables: MutableList<Interactable>,
    val collisionBodies: MutableList<Body>,
    val warpStarts: List<WarpStart>,
    val warpExits: List<WarpExit>
) {
    val width: Int = tiledMap.properties["width"] as Int * tiledMap.properties["tilewidth"] as Int
    val height: Int = tiledMap.properties["height"] as Int * tiledMap.properties["tileheight"] as Int
}