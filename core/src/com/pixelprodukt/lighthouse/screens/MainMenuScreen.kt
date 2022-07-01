package com.pixelprodukt.lighthouse.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ScalingViewport
import com.pixelprodukt.lighthouse.constants.GameConfig
import com.pixelprodukt.lighthouse.enums.GameScreen
import com.pixelprodukt.lighthouse.system.GameManager
import com.pixelprodukt.lighthouse.ui.`scene2d-widget`.TextButtonWithMarker
import ktx.app.KtxScreen
import ktx.app.clearScreen

class MainMenuScreen(private val game: GameManager) : KtxScreen {

    private val camera = OrthographicCamera(GameConfig.VIEWPORT_WIDTH, GameConfig.VIEWPORT_HEIGHT)
    private val stage = Stage(ScalingViewport(Scaling.fill, GameConfig.VIEWPORT_WIDTH, GameConfig.VIEWPORT_HEIGHT ,camera))
    private val layoutTable = Table()
    private val startBtn: TextButtonWithMarker = TextButtonWithMarker("Start", game.fontskin)
    private val optionsBtn: TextButtonWithMarker = TextButtonWithMarker("Options", game.fontskin)
    private val quitBtn: TextButtonWithMarker = TextButtonWithMarker("Quit", game.fontskin)

    init {
        layoutTable.setFillParent(true)
        layoutTable.add(startBtn).align(Align.center)
        layoutTable.row()
        layoutTable.add(optionsBtn).align(Align.center)
        layoutTable.row()
        layoutTable.add(quitBtn).align(Align.center)
        stage.addActor(layoutTable)
        stage.keyboardFocus = layoutTable.getChild(0)

        startBtn.addListener(object: InputListener() {
            override fun keyUp(event: InputEvent?, keycode: Int): Boolean {
                if (keycode == Input.Keys.E) {
                    game.setScreen(GameScreen.WORLD)
                }
                return true
            }
        })

        quitBtn.addListener(object: InputListener() {
            override fun keyUp(event: InputEvent?, keycode: Int): Boolean {
                if (keycode == Input.Keys.E) {
                    Gdx.app.exit()
                }
                return true
            }
        })
    }

    private fun nextButton() {
        val parent = stage.keyboardFocus.parent
        val children = parent.children

        if ((children.indexOf(stage.keyboardFocus) + 1) == children.size) {
            stage.keyboardFocus = parent.getChild(0)
        } else {
            stage.keyboardFocus = parent.getChild(children.indexOf(stage.keyboardFocus) + 1)
            if (stage.keyboardFocus is Label) nextButton()
        }
    }

    private fun previousButton() {
        val parent = stage.keyboardFocus.parent
        val children = parent.children

        if ((children.indexOf(stage.keyboardFocus) - 1) < 0) {
            stage.keyboardFocus = parent.getChild(children.size - 1)
            if (stage.keyboardFocus is Label) previousButton()
        } else {
            stage.keyboardFocus = parent.getChild(children.indexOf(stage.keyboardFocus) - 1)
            if (stage.keyboardFocus is Label) previousButton()
        }
    }

    override fun render(delta: Float) {
        clearScreen(224f / 255f, 248f / 255f, 208f / 255f, 1f)

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) previousButton()
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) nextButton()

        camera.position.set(GameConfig.VIEWPORT_WIDTH / 2, GameConfig.VIEWPORT_HEIGHT / 2, 0f)
        camera.update()
        stage.act(Gdx.graphics.deltaTime)
        stage.draw()
    }

    override fun show() {
        super.show()
        Gdx.input.inputProcessor = stage
    }
}