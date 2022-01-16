package com.pixelprodukt.lighthouse.screens

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pixelprodukt.lighthouse.system.GameManager
import ktx.app.clearScreen
import ktx.graphics.use

class BattleScreen(private val game: GameManager) : Screen {

    private val batch = SpriteBatch()
    private val camera = OrthographicCamera(1024f / 4f, 768f / 4f)

    init {
    }

    override fun render(delta: Float) {
        clearScreen(8f / 255f, 24f / 255f, 32f / 255f, 1f)
        camera.position.set(256f / 2, 192f / 2, 0f)
        camera.update()
        batch.projectionMatrix = camera.combined;


        batch.use { spriteBatch ->
        }
    }

    override fun show() {}

    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {}
}