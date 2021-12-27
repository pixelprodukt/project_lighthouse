package com.pixelprodukt.lighthouse.screens

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.pixelprodukt.lighthouse.gameobjects.GameObject
import com.pixelprodukt.lighthouse.map.GameMap
import com.pixelprodukt.lighthouse.map.WarpStart
import com.pixelprodukt.lighthouse.system.*
import ktx.app.clearScreen
import ktx.graphics.use

class GameScreen(private val game: GameManager) : Screen {

    private val batch = game.batch
    private val shapeRenderer = game.shapeRenderer
    private val camera = game.camera
    private val player = game.player
    private var currentMap = game.mapHandler.getGameMap("test_01")
    private val mapRenderer = OrthogonalTiledMapRenderer(currentMap.tiledMap)

    init {
        batch.projectionMatrix = camera.combined
        mapRenderer.map = currentMap.tiledMap
        mapRenderer.setView(camera)
    }

    override fun render(delta: Float) {
        clearScreen(8f / 255f, 24f / 255f, 32f / 255f, 1f)

        handleInput()
        processCollisions(currentMap.gameObjects, currentMap.collisionBodies)
        processWarpCollisions(player, currentMap)

        camera.position.x = 256f / 2
        camera.position.y = 192f / 2
        camera.update()

        mapRenderer.setView(camera)

        camera.position.set(player.transform.position.x, player.transform.position.y, 0f)
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

        batch.projectionMatrix = camera.combined;

        mapRenderer.render()

        batch.use {
            currentMap.gameObjects.forEach { gameObject ->
                gameObject.update()
                gameObject.render(batch)
            }
        }

        if (game.inputHandler.isDebug) {
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

                currentMap.gameObjects.forEach { character ->
                    renderer.rect(
                        character.body.position.x + character.body.offset.x,
                        character.body.position.y + character.body.offset.y,
                        character.body.size.x,
                        character.body.size.y
                    )
                }
            }
        }
        currentMap.gameObjects.sort()
    }

    override fun show() {}

    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {}

    private fun handleInput() {

        player.body.velocity.set(0f, 0f)

        if (game.inputHandler.isUpPressed) player.body.velocity.y = 1f
        if (game.inputHandler.isDownPressed) player.body.velocity.y = -1f
        if (game.inputHandler.isLeftPressed) player.body.velocity.x = -1f
        if (game.inputHandler.isRightPressed) player.body.velocity.x = 1f

        player.body.velocity.nor()
        player.body.position.x += player.body.velocity.x * player.speed
        player.body.position.y += player.body.velocity.y * player.speed
    }

    private fun processCollisions(collidables: MutableList<GameObject>, staticBodies: MutableList<Body>) {
        collidables.forEach { collidable ->
            staticBodies.forEach { body ->
                resolveCollision(collidable.body, body)
            }
        }
    }

    private fun processWarpCollisions(player: GameObject, gameMap: GameMap) {

        gameMap.warpStarts.forEach { warp ->

            if (intersect(player.body, warp.body)) {

                currentMap = game.mapHandler.getGameMap(warp.targetMapName)
                mapRenderer.map = currentMap.tiledMap

                val exit = currentMap.warpExits.find { exit -> exit.id == warp.exitId } ?: throw Exception("No warp exit found!")

                if (warpIsHorizontal(warp, currentMap)) {
                    player.body.position.set(exit.body.center.x, player.body.position.y)
                } else {
                    player.body.position.set(player.body.position.x, exit.body.center.y + (player.body.size.y / 2))
                }
            }
        }
    }

    private fun warpIsHorizontal(warp: WarpStart, map: GameMap): Boolean {
        return (warp.body.position.x >= map.width || (warp.body.position.x + warp.body.size.x) <= map.width) &&
                ((warp.body.position.y + warp.body.size.y) <= map.height && warp.body.position.y > 0)
    }

    /**
     * Utility function
     */
    private fun clamp(value: Float, max: Float, min: Float): Float {
        return if (value > min) {
            if (value < max) {
                value
            } else max
        } else min
    }
}