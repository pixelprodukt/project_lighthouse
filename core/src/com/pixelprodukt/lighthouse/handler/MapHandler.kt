package com.pixelprodukt.lighthouse.handler

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.pixelprodukt.lighthouse.gameobjects.Chest
import com.pixelprodukt.lighthouse.gameobjects.GameObject
import com.pixelprodukt.lighthouse.gameobjects.Sign
import com.pixelprodukt.lighthouse.map.GameMap
import com.pixelprodukt.lighthouse.map.WarpExit
import com.pixelprodukt.lighthouse.map.WarpFactory
import com.pixelprodukt.lighthouse.map.WarpStart
import com.pixelprodukt.lighthouse.system.Body

class MapHandler(val player: GameObject) {

    private val mapnamesToLoad = listOf<String>("test_01", "test_02", "test_03")
    private val mapLoader = TmxMapLoader()
    private val warpFactory = WarpFactory()
    private val mapsDictionary = HashMap<String, GameMap>()

    fun initMaps() {
        mapnamesToLoad.forEach { name ->
            mapsDictionary[name] = buildGameMap(name)
        }
    }

    private fun buildGameMap(mapName: String): GameMap {

        val tiledMap = mapLoader.load("maps/$mapName.tmx")
        val gameObjects = initGameObjects(tiledMap)
        val collisionBodies = initCollisionBodies(tiledMap)
        gameObjects.forEach { gameObject -> collisionBodies.add(gameObject.body) }

        return GameMap(
            mapName,
            tiledMap,
            gameObjects,
            collisionBodies,
            initWarpStarts(tiledMap),
            initWarpExits(tiledMap)
        )
    }

    fun getGameMap(mapName: String): GameMap {
        return mapsDictionary[mapName] ?: throw Exception("Map not found in dictionary")
    }

    private fun initGameObjects(map: TiledMap): MutableList<GameObject> {

        val gameObjects = mutableListOf<GameObject>()
        val mapGameObjects = map.layers.get("objects")?.objects?.getByType(TiledMapTileMapObject::class.java)

        mapGameObjects?.forEach { gameObject ->
            if (gameObject.name == "sign") {
                val sign = Sign()
                sign.body.position.set(gameObject.x, gameObject.y)
                sign.body.size.set(16f, 6f)
                sign.transform.offset.set(Vector2(8f, 8f))
                sign.body.isStatic = true
                gameObjects.add(sign)
            }
            if (gameObject.name == "chest") {
                val chest = Chest()
                chest.body.position.set(gameObject.x, gameObject.y)
                chest.body.size.set(16f, 10f)
                chest.transform.offset.set(8f, 8f)
                chest.body.isStatic = true
                gameObjects.add(chest)
            }
        }

        gameObjects.add(player)
        return gameObjects
    }

    private fun initCollisionBodies(map: TiledMap): MutableList<Body> {

        val collisionBodies = mutableListOf<Body>()

        val rectangleList = map.layers.get("collisions")?.objects?.getByType(RectangleMapObject::class.java)
            ?: throw Exception("No collision layer found")

        rectangleList.forEach { rectangleMapObject ->

            val rect = rectangleMapObject.rectangle
            val body = Body(Vector2(rect.x, rect.y), Vector2(rect.width, rect.height))
            body.isStatic = true
            collisionBodies.add(body)
        }
        return collisionBodies
    }

    private fun initWarpStarts(map: TiledMap): List<WarpStart> {

        val rectangleList = map.layers.get("warps")?.objects?.getByType(RectangleMapObject::class.java)
            ?: throw Exception("No warps found")

        return rectangleList
            .filter { rectangleMapObject -> rectangleMapObject.properties["type"] == "start" }
            .map { rectangleMapObject ->
                warpFactory.createWarpStart(rectangleMapObject)
            }.toList()
    }

    private fun initWarpExits(map: TiledMap): List<WarpExit> {

        val rectangleList = map.layers.get("warps")?.objects?.getByType(RectangleMapObject::class.java)
            ?: throw Exception("No warps found")

        return rectangleList
            .filter { rectangleMapObject -> rectangleMapObject.properties["type"] == "exit" }
            .map { rectangleMapObject ->
                warpFactory.createWarpExit(rectangleMapObject)
            }.toList()
    }
}