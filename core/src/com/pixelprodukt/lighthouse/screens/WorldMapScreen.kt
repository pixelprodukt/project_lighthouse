package com.pixelprodukt.lighthouse.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.pixelprodukt.lighthouse.Behaviour
import com.pixelprodukt.lighthouse.BehaviourType
import com.pixelprodukt.lighthouse.UpdateState
import com.pixelprodukt.lighthouse.enums.WorldMapState
import com.pixelprodukt.lighthouse.gameobjects.*
import com.pixelprodukt.lighthouse.gameobjects.itemdata.Item
import com.pixelprodukt.lighthouse.handler.MessageHandler
import com.pixelprodukt.lighthouse.interfaces.Interactable
import com.pixelprodukt.lighthouse.map.GameMap
import com.pixelprodukt.lighthouse.system.*
import com.pixelprodukt.lighthouse.ui.SimpleTextBox
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.graphics.use

class WorldMapScreen(private val game: GameManager) : KtxScreen {

    private val console = Console(WorldMapScreen::class.simpleName!!)
    private val batch = SpriteBatch()
    private val camera = OrthographicCamera(1024f / 4f, 768f / 4f)
    private val uiBatch = SpriteBatch()
    private val uiCamera = OrthographicCamera(1024f / 4f, 768f / 4f)
    private val shapeRenderer = ShapeRenderer()
    private val player = game.player
    private var currentMap = game.mapHandler.getGameMap("test_01")
    private val mapRenderer = OrthogonalTiledMapRenderer(currentMap.tiledMap)
    private var state: WorldMapState = WorldMapState.RUNNING
    private val interactablesInRange = mutableListOf<Interactable>()
    private val simpleTextBox = SimpleTextBox(248f, 45f)
    private val inputListener = InputListener()

    init {
        batch.projectionMatrix = camera.combined
        mapRenderer.map = currentMap.tiledMap
        mapRenderer.setView(camera)
        initInteractableListeners(currentMap)
        simpleTextBox.onClose { state = WorldMapState.RUNNING }
        // game.testNpc.setBoundaries(currentMap.tiledMap) // TODO: Boundaries have to be set when the npc is put into the GameMap object
        currentMap.mountObjects()

        currentMap.startCutscene(listOf(
            Behaviour(BehaviourType.WALK, Direction.RIGHT, characterId = "PLAYER"),
            Behaviour(BehaviourType.WALK, Direction.RIGHT, characterId = "PLAYER"),
            Behaviour(BehaviourType.WALK, Direction.DOWN, characterId = "PLAYER"),
            Behaviour(BehaviourType.WALK, Direction.DOWN, characterId = "PLAYER"),
            Behaviour(BehaviourType.WALK, Direction.LEFT, characterId = "NADJA"),
            Behaviour(BehaviourType.WALK, Direction.DOWN, characterId = "NADJA"),
            Behaviour(BehaviourType.WALK, Direction.DOWN, characterId = "NADJA"),
        ))
    }

    override fun render(delta: Float) {
        clearScreen(8f / 255f, 24f / 255f, 32f / 255f, 1f)

        currentMap.interactables.forEach { interactable ->
            /*if (interactable.sensor.isActive && intersect(player.body, interactable.sensor)) {
                interactablesInRange.add(interactable)
            }*/
        }

        handleInput()
        // processCollisions(currentMap.walls)
        // processWarpCollisions(player, currentMap)

        camera.update()
        camera.position.set(player.x, player.y, 0f)
        clampCamera(camera, currentMap)
        batch.projectionMatrix = camera.combined;
        mapRenderer.setView(camera)
        mapRenderer.render()

        batch.use { batch ->
            currentMap.gameObjects.forEach { gameObject ->
                if (state == WorldMapState.RUNNING) gameObject.update(UpdateState(game.inputHandler.pressedKey, currentMap))
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

    override fun show() {
        super.show()

        MessageHandler.addMessageListener { messages ->
            state = WorldMapState.TEXTBOX_INTERACTION
            simpleTextBox.init(messages)
        }

        Gdx.input.inputProcessor = game.inputHandler
    }

    override fun hide() {
        MessageHandler.removeAllListeners()
    }

    private fun handleInput() {

        if (state == WorldMapState.RUNNING) {

            if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
                MessageHandler.publishMessages(mutableListOf("Hello World from WorldMapView!", "This is a message for you all!"))
            }

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
                    val messages: MutableList<String> = mutableListOf()
                    val items = event as MutableList<Item>
                    items.forEach { item ->
                        //player.inventory.addItem(item)
                        messages.add("You found ${item.quantity} ${item.label}")
                    }
                    simpleTextBox.init(messages)
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
            /*if (intersect(player.body, warp.body)) {
                currentMap.interactables.forEach { interactable -> interactable.removeAllListeners() }
                currentMap = game.mapHandler.getGameMap(warp.targetMapName)
                initInteractableListeners(currentMap)
                mapRenderer.map = currentMap.tiledMap

                val entry = currentMap.warpEntries.find { exit -> exit.id == warp.targetEntryId }
                    ?: throw Exception("No warp exit found!")

                player.body.xy(entry.body.center.x - (player.body.width / 2), entry.body.center.y - (player.body.height / 2))
            }*/
        }
    }

    private fun debugRendering() {
        camera.update()
        shapeRenderer.projectionMatrix = camera.combined
        shapeRenderer.use(ShapeRenderer.ShapeType.Line) { renderer ->
            renderer.color = Color.RED

            currentMap.walls.forEach { wall ->
                renderer.rect(
                    wall.x,
                    wall.y,
                    wall.width,
                    wall.height
                )
            }
            currentMap.gameObjects.forEach { gameObject ->
                renderer.rect(
                    gameObject.x,
                    gameObject.y,
                    gameObject.width,
                    gameObject.height
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