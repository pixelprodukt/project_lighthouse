package com.pixelprodukt.lighthouse.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.pixelprodukt.lighthouse.enums.WorldMapState
import com.pixelprodukt.lighthouse.gameobjects.Chest
import com.pixelprodukt.lighthouse.gameobjects.GameObject
import com.pixelprodukt.lighthouse.gameobjects.Sign
import com.pixelprodukt.lighthouse.gameobjects.characterdata.Item
import com.pixelprodukt.lighthouse.interfaces.Interactable
import com.pixelprodukt.lighthouse.map.GameMap
import com.pixelprodukt.lighthouse.map.WarpStart
import com.pixelprodukt.lighthouse.system.*
import com.pixelprodukt.lighthouse.ui.SimpleTextBox
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import ktx.app.clearScreen
import ktx.graphics.use

class WorldMapScreen(private val game: GameManager) : Screen {

    private val console = Console(WorldMapScreen::class.simpleName!!)
    private val batch = game.batch
    private val shapeRenderer = game.shapeRenderer
    private val camera = game.camera
    private val player = game.player
    private var currentMap = game.mapHandler.getGameMap("test_01")
    private val mapRenderer = OrthogonalTiledMapRenderer(currentMap.tiledMap)
    private var state: WorldMapState = WorldMapState.RUNNING

    private val simpleTextBox = SimpleTextBox()

    init {
        batch.projectionMatrix = camera.combined
        mapRenderer.map = currentMap.tiledMap
        mapRenderer.setView(camera)
        initInteractableListeners(currentMap)
        simpleTextBox.onClose { state = WorldMapState.RUNNING }
    }

    override fun render(delta: Float) {
        clearScreen(8f / 255f, 24f / 255f, 32f / 255f, 1f)

        handleInput()
        processCollisions(currentMap.collisionBodies)
        processWarpCollisions(player, currentMap)

        camera.position.x = 256f / 2
        camera.position.y = 192f / 2
        camera.update()
        camera.position.set(player.transform.position.x, player.transform.position.y, 0f)
        clampCamera(camera, currentMap)
        batch.projectionMatrix = camera.combined;
        mapRenderer.setView(camera)
        mapRenderer.render()

        batch.use {
            currentMap.gameObjects.forEach { gameObject ->
                if (state == WorldMapState.RUNNING) gameObject.update()
                gameObject.render(batch)
            }

            if (state == WorldMapState.TEXTBOX_INTERACTION) {
                simpleTextBox.render(batch)
            }
        }

        if (game.inputHandler.isDebug) debugRendering()

        currentMap.gameObjects.sort()
    }

    override fun show() {}

    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {}

    private fun handleInput() {

        if (state == WorldMapState.RUNNING) {
            player.body.velocity.set(0f, 0f)

            /*if (game.inputHandler.isUpPressed) player.body.velocity.y = 1f
            if (game.inputHandler.isDownPressed) player.body.velocity.y = -1f
            if (game.inputHandler.isLeftPressed) player.body.velocity.x = -1f
            if (game.inputHandler.isRightPressed) player.body.velocity.x = 1f*/

            if (Gdx.input.isKeyPressed(Input.Keys.W)) player.body.velocity.y = 1f
            if (Gdx.input.isKeyPressed(Input.Keys.S)) player.body.velocity.y = -1f
            if (Gdx.input.isKeyPressed(Input.Keys.A)) player.body.velocity.x = -1f
            if (Gdx.input.isKeyPressed(Input.Keys.D)) player.body.velocity.x = 1f

            player.body.velocity.nor()
            player.body.position.x += player.body.velocity.x * player.speed
            player.body.position.y += player.body.velocity.y * player.speed

            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                currentMap.interactables.forEach { interactable ->
                    if (intersect(player.body, interactable.sensor)) {
                        interactable.interact()
                    }
                }
            }

        } else if (state == WorldMapState.TEXTBOX_INTERACTION) {

            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                simpleTextBox.nextMessage()
            }
        }
    }

    private fun initInteractableListeners(map: GameMap) {
        map.interactables.forEach { interactable ->
            if (interactable is Sign) {
                interactable.addInteractionListener { event ->
                    console.log("interact with Sign: ${event as String}")
                    state = WorldMapState.TEXTBOX_INTERACTION
                    simpleTextBox.init(mutableListOf(interactable.text))
                }
            }
            if (interactable is Chest) {
                interactable.addInteractionListener { event ->
                    console.log("interact with Chest: ${event as MutableList<Item>}")
                    state = WorldMapState.TEXTBOX_INTERACTION
                    simpleTextBox.init(mutableListOf("This is text from the chest.", "You found 2 Items.", "Whatever!"))
                }
            }
        }
    }

    private fun processCollisions(bodies: MutableList<Body>) {
        bodies.forEach { body ->
            bodies.forEach { other ->
                if (body != other && !body.isStatic && other.isStatic) resolveCollision(body, other)
            }
        }
    }

    private fun processWarpCollisions(player: GameObject, gameMap: GameMap) {

        gameMap.warpStarts.forEach { warp ->

            if (intersect(player.body, warp.body)) {

                currentMap.interactables.forEach { interactable -> interactable.deleteAllListeners() }

                currentMap = game.mapHandler.getGameMap(warp.targetMapName)
                initInteractableListeners(currentMap)
                mapRenderer.map = currentMap.tiledMap

                val exit = currentMap.warpExits.find { exit -> exit.id == warp.exitId }
                    ?: throw Exception("No warp exit found!")

                if (warpIsHorizontal(warp, currentMap)) {
                    player.body.position.set(exit.body.center.x - (player.body.size.x / 2), player.body.position.y)
                } else {
                    player.body.position.set(player.body.position.x, exit.body.center.y - (player.body.size.y / 2))
                }
            }
        }
    }

    private fun warpIsHorizontal(warp: WarpStart, map: GameMap): Boolean {
        return (warp.body.position.x >= map.width || (warp.body.position.x + warp.body.size.x) <= map.width) &&
                ((warp.body.position.y + warp.body.size.y) <= map.height && warp.body.position.y > 0)
    }

    private fun debugRendering() {
        camera.update()
        shapeRenderer.projectionMatrix = camera.combined

        shapeRenderer.use(ShapeRenderer.ShapeType.Line) { renderer ->
            renderer.color = Color.RED

            currentMap.collisionBodies.forEach { body ->
                renderer.rect(
                    body.position.x + body.offset.x,
                    body.position.y + body.offset.y,
                    body.size.x,
                    body.size.y
                )
            }

            currentMap.gameObjects.forEach { gameObject ->
                renderer.rect(
                    gameObject.body.position.x + gameObject.body.offset.x,
                    gameObject.body.position.y + gameObject.body.offset.y,
                    gameObject.body.size.x,
                    gameObject.body.size.y
                )
            }

            renderer.color = Color.CYAN
            currentMap.gameObjects.forEach { gameObject ->
                if (gameObject is Interactable) {
                    renderer.rect(
                        gameObject.sensor.position.x + gameObject.sensor.offset.x,
                        gameObject.sensor.position.y + gameObject.sensor.offset.y,
                        gameObject.sensor.size.x,
                        gameObject.sensor.size.y
                    )
                }
            }
        }
    }

    /**
     * Utility functions
     */
    private fun clamp(value: Float, max: Float, min: Float): Float {
        return if (value > min) {
            if (value < max) {
                value
            } else max
        } else min
    }

    private fun clampCamera(camera: OrthographicCamera, currentMap: GameMap) {
        camera.position.x = clamp(
            camera.position.x,
            currentMap.width.toFloat() - (camera.viewportWidth / 2),
            0 + (camera.viewportWidth / 2)
        )
        camera.position.y = clamp(
            camera.position.y,
            currentMap.height.toFloat() - (camera.viewportHeight / 2),
            0 + (camera.viewportHeight / 2)
        )
    }
}