package com.pixelprodukt.lighthouse.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.pixelprodukt.lighthouse.enums.WorldMapState
import com.pixelprodukt.lighthouse.gameobjects.Chest
import com.pixelprodukt.lighthouse.gameobjects.GameObject
import com.pixelprodukt.lighthouse.gameobjects.Sign
import com.pixelprodukt.lighthouse.gameobjects.SimpleNpcCharacter
import com.pixelprodukt.lighthouse.interfaces.Interactable
import com.pixelprodukt.lighthouse.map.GameMap
import com.pixelprodukt.lighthouse.map.WarpEntry
import com.pixelprodukt.lighthouse.map.WarpExit
import com.pixelprodukt.lighthouse.system.*
import com.pixelprodukt.lighthouse.ui.SimpleTextBox
import ktx.app.clearScreen
import ktx.graphics.use

class WorldMapScreen(private val game: GameManager) : Screen {

    private val console = Console(WorldMapScreen::class.simpleName!!)
    private val batch = SpriteBatch()
    private val camera = OrthographicCamera(1024f / 4f, 768f / 4f)
    private val uiBatch = SpriteBatch()
    private val uiCamera = OrthographicCamera(1024f / 4f, 768f / 4f)
    private val shapeRenderer = ShapeRenderer()
    private val player = game.player
    private var currentMap = game.mapHandler.getGameMap("test_04")
    private val mapRenderer = OrthogonalTiledMapRenderer(currentMap.tiledMap)
    private var state: WorldMapState = WorldMapState.RUNNING
    private val interactablesInRange = mutableListOf<Interactable>()
    private val simpleTextBox = SimpleTextBox()

    init {
        batch.projectionMatrix = camera.combined
        mapRenderer.map = currentMap.tiledMap
        mapRenderer.setView(camera)
        initInteractableListeners(currentMap)
        simpleTextBox.onClose { state = WorldMapState.RUNNING }
        game.testNpc.setBoundaries(currentMap.tiledMap)
    }

    override fun render(delta: Float) {
        clearScreen(8f / 255f, 24f / 255f, 32f / 255f, 1f)

        currentMap.interactables.forEach { interactable ->
            if (interactable.sensor.isActive && intersect(player.body, interactable.sensor)) {
                interactablesInRange.add(interactable)
            }
        }

        handleInput()
        processCollisions(currentMap.collisionBodies)
        processWarpCollisions(player, currentMap)

        camera.update()
        camera.position.set(player.transform.position.x, player.transform.position.y, 0f)
        clampCamera(camera, currentMap)
        batch.projectionMatrix = camera.combined;
        mapRenderer.setView(camera)
        mapRenderer.render()

        batch.use { batch ->
            currentMap.gameObjects.forEach { gameObject ->
                if (state == WorldMapState.RUNNING) gameObject.update()
                gameObject.render(batch)
            }
        }

        uiCamera.update()
        uiCamera.position.set(256f / 2, 192f / 2, 0f)
        uiBatch.projectionMatrix = uiCamera.combined
        uiBatch.use { uiBatch ->
            if (state == WorldMapState.TEXTBOX_INTERACTION) {
                simpleTextBox.render(uiBatch)
            }
        }

        if (game.inputHandler.isDebug) debugRendering()

        currentMap.gameObjects.sort()
        interactablesInRange.clear()
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

            player.body.velocity.normalized
            player.body.x += player.body.velocity.x * player.speed
            player.body.y += player.body.velocity.y * player.speed

            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                if (interactablesInRange.isNotEmpty()) interactablesInRange.last().interact()
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
                    state = WorldMapState.TEXTBOX_INTERACTION
                    simpleTextBox.init(mutableListOf(interactable.text))
                }
            }
            if (interactable is Chest) {
                interactable.addInteractionListener { event ->
                    state = WorldMapState.TEXTBOX_INTERACTION
                    simpleTextBox.init(mutableListOf("This is text from the chest.", "You found 2 Items.", "Whatever!"))
                    interactable.sensor.isActive = false
                }
            }
            if (interactable is SimpleNpcCharacter) {
                interactable.addInteractionListener { event ->
                    state = WorldMapState.TEXTBOX_INTERACTION
                    simpleTextBox.init(event as MutableList<String>)
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

        gameMap.warpExits.forEach { warp ->

            if (intersect(player.body, warp.body)) {

                currentMap.interactables.forEach { interactable -> interactable.deleteAllListeners() }

                currentMap = game.mapHandler.getGameMap(warp.targetMapName)
                initInteractableListeners(currentMap)
                mapRenderer.map = currentMap.tiledMap

                val entry = currentMap.warpEntries.find { exit -> exit.id == warp.targetEntryId }
                    ?: throw Exception("No warp exit found!")

                player.body.xy(entry.body.center.x - (player.body.width / 2), entry.body.center.y - (player.body.height / 2))

                /*if (warpIsHorizontal(warp, currentMap)) {
                    player.body.xy(entry.body.center.x - (player.body.width / 2), player.body.y)
                } else {
                    player.body.xy(player.body.x, entry.body.center.y - (player.body.height / 2))
                }*/
            }
        }
    }

    private fun warpIsHorizontal(warp: WarpExit, map: GameMap): Boolean {
        return (warp.body.x >= map.width || (warp.body.x + warp.body.width) <= map.width) &&
                ((warp.body.y + warp.body.height) <= map.height && warp.body.y > 0)
    }

    private fun debugRendering() {
        camera.update()
        shapeRenderer.projectionMatrix = camera.combined

        shapeRenderer.use(ShapeRenderer.ShapeType.Line) { renderer ->
            renderer.color = Color.RED

            currentMap.collisionBodies.forEach { body ->
                renderer.rect(
                    body.x + body.offset.x,
                    body.y + body.offset.y,
                    body.width,
                    body.height
                )
            }

            currentMap.gameObjects.forEach { gameObject ->
                renderer.rect(
                    gameObject.body.x + gameObject.body.offset.x,
                    gameObject.body.y + gameObject.body.offset.y,
                    gameObject.body.width,
                    gameObject.body.height
                )
            }

            renderer.color = Color.CYAN
            currentMap.gameObjects.forEach { gameObject ->
                if (gameObject is Interactable) {
                    renderer.rect(
                        gameObject.sensor.x + gameObject.sensor.offset.x,
                        gameObject.sensor.y + gameObject.sensor.offset.y,
                        gameObject.sensor.width,
                        gameObject.sensor.height
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