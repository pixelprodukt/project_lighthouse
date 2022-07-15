package com.pixelprodukt.lighthouse

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.Json
import com.google.gson.Gson
import com.pixelprodukt.lighthouse.screens.MainMenuScreen
import com.pixelprodukt.lighthouse.screens.SceneTestScreen
import com.pixelprodukt.lighthouse.screens.WorldMapScreen
import com.pixelprodukt.lighthouse.system.GameManager
import ktx.async.KtxAsync

class ProjectLighthouse : Game() {

    private lateinit var game: GameManager

    lateinit var worldMapScreen: WorldMapScreen
    lateinit var mainMenuScreen: MainMenuScreen
    lateinit var testScreen: SceneTestScreen

    override fun create() {
        KtxAsync.initiate()
        game = GameManager(this)
        worldMapScreen = WorldMapScreen(game)
        mainMenuScreen = MainMenuScreen(game)
        testScreen = SceneTestScreen(game)

        setScreen(mainMenuScreen)
    }

    override fun render() {
        screen.render(Gdx.graphics.deltaTime)

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) setScreen(mainMenuScreen)
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) setScreen(worldMapScreen)
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) setScreen(testScreen)
    }

    /*override fun dispose() {
        gameScreen.dispose()
    }*/
}
