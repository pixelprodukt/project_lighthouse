package com.pixelprodukt.lighthouse.handler

import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject
import com.pixelprodukt.lighthouse.enums.ItemType
import com.pixelprodukt.lighthouse.data.MapChestObjectItemData
import com.pixelprodukt.lighthouse.gameobjects.GameObject
import com.pixelprodukt.lighthouse.gameobjects.Wall
import com.pixelprodukt.lighthouse.gameobjects.itemdata.Item
import com.pixelprodukt.lighthouse.interfaces.Interactable
import com.pixelprodukt.lighthouse.map.GameMap
import com.pixelprodukt.lighthouse.map.WarpExit
import com.pixelprodukt.lighthouse.map.WarpFactory
import com.pixelprodukt.lighthouse.map.WarpEntry
import com.pixelprodukt.lighthouse.system.Body
import com.pixelprodukt.lighthouse.system.GameManager
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class MapHandler(private val game: GameManager) {

    val player = game.player
    private val mapnamesToLoad = listOf<String>("test_01", "test_02", "test_03", "test_04")
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
        val walls = initWalls(tiledMap)

        return GameMap(
            mapName,
            tiledMap,
            gameObjects,
            initInteractables(gameObjects),
            walls,
            initWarpEntries(tiledMap),
            initWarpExits(tiledMap)
        )
    }

    fun getGameMap(mapName: String): GameMap {
        return mapsDictionary[mapName] ?: throw Exception("Map not found in dictionary")
    }

    private fun initInteractables(gameObjects: MutableList<GameObject>): MutableList<Interactable> {
        val interactables: MutableList<Interactable> = mutableListOf()
        gameObjects.forEach { gameObject -> if (gameObject is Interactable) interactables.add(gameObject) }
        return interactables
    }

    private fun initGameObjects(map: TiledMap): MutableList<GameObject> {

        val gameObjects = mutableListOf<GameObject>()
        val mapGameObjects = map.layers.get("objects")?.objects?.getByType(TiledMapTileMapObject::class.java)

        mapGameObjects?.forEach { gameObject ->
            if (gameObject.name == "sign") {
                /*val sign = Sign(gameObject.properties["text"] as String)
                sign.body.xy(gameObject.x, gameObject.y)
                sign.body.size(16f, 6f)
                sign.transform.offset.set(Vector2(8f, 8f))
                sign.body.isStatic = true
                sign.sensor.size(24f, 24f)
                sign.sensor.xy((sign.body.x + (sign.body.width / 2)) - sign.sensor.width / 2, (sign.body.y + (sign.body.height / 2)) - sign.sensor.height / 2)
                gameObjects.add(sign)*/
            }
            if (gameObject.name == "chest") {
                val items = parseItemsFromMapChestObject(gameObject) /*mutableListOf(
                    Item("s. Health Potion", ItemType.HEALING_POTION_S, true, false, false, false, false, 2, 8, 12),
                    Item("l. Health Potion", ItemType.HEALING_POTION_M, true, false, false, false, false, 4, 14, 20)
                )*/
                /*val chest = Chest(items)
                chest.body.xy(gameObject.x, gameObject.y)
                chest.body.size(16f, 10f)
                chest.transform.offset.set(8f, 8f)
                chest.body.isStatic = true
                chest.sensor.size(24f, 24f)
                chest.sensor.xy((chest.body.x + (chest.body.width / 2)) - chest.sensor.width / 2, (chest.body.y + (chest.body.height / 2)) - chest.sensor.height / 2)
                gameObjects.add(chest)*/
            }
        }

        gameObjects.add(player)
        gameObjects.add(game.testNpc)
        return gameObjects
    }

    private fun parseItemsFromMapChestObject(gameObject: TiledMapTileMapObject): MutableList<Item> {
        val property = gameObject.properties["items"] as String
        val itemData = Json.decodeFromString<MutableList<MapChestObjectItemData>>(property)
        val items: MutableList<Item> = mutableListOf()
        itemData.forEach { data -> items.add(Item(data.label, ItemType.valueOf(data.type), data.quantity)) }
        return items
    }

    private fun initWalls(map: TiledMap): MutableList<Wall> {

        val walls = mutableListOf<Wall>()

        val rectangleList = map.layers.get("collisions")?.objects?.getByType(RectangleMapObject::class.java)
            ?: throw Exception("No collision layer found")

        rectangleList.forEach { rectangleMapObject ->
            val rect = rectangleMapObject.rectangle
            val wall = Wall(rect.x, rect.y)
            walls.add(wall)
        }
        return walls
    }

    private fun initWarpEntries(map: TiledMap): List<WarpEntry> {

        val rectangleList = map.layers.get("warps")?.objects?.getByType(RectangleMapObject::class.java)
            ?: throw Exception("No warps found")

        return rectangleList
            .filter { rectangleMapObject -> rectangleMapObject.properties["type"] == "entry" }
            .map { rectangleMapObject ->
                warpFactory.createWarpEntry(rectangleMapObject)
            }.toList()
    }

    private fun initWarpExits(map: TiledMap): List<WarpExit> {

        val rectangleList = map.layers.get("warps")?.objects?.getByType(RectangleMapObject::class.java)
            ?: throw Exception("No warps found")

        return rectangleList
            .filter { rectangleMapObject -> rectangleMapObject. properties["type"] == "exit" }
            .map { rectangleMapObject ->
                warpFactory.createWarpExit(rectangleMapObject)
            }.toList()
    }
}