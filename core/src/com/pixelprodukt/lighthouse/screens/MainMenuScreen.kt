package com.pixelprodukt.lighthouse.screens

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pixelprodukt.lighthouse.system.GameManager
import ktx.app.clearScreen

class MainMenuScreen(private val context: GameManager) : Screen {

    private val batch = SpriteBatch()
    private val camera = OrthographicCamera(1024f / 4f, 768f / 4f)

    init {
        batch.projectionMatrix = camera.combined
        // Gdx.input.inputProcessor = context.inputHandler
    }

    override fun show() {
        // TODO("Not yet implemented")
    }

    override fun render(delta: Float) {
        clearScreen(224f / 255f, 248f / 255f, 208f / 255f, 1f)
        batch.projectionMatrix = camera.combined;
    }

    override fun resize(width: Int, height: Int) {
        // TODO("Not yet implemented")
    }

    override fun pause() {
        // TODO("Not yet implemented")
    }

    override fun resume() {
        // TODO("Not yet implemented")
    }

    override fun hide() {
        // TODO("Not yet implemented")
    }

    override fun dispose() {
        // TODO("Not yet implemented")
    }
}