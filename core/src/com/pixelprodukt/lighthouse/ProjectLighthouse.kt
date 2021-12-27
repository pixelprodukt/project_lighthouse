package com.pixelprodukt.lighthouse

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.pixelprodukt.lighthouse.screens.BattleScreen
import com.pixelprodukt.lighthouse.screens.GameScreen
import com.pixelprodukt.lighthouse.screens.MainMenuScreen
import com.pixelprodukt.lighthouse.system.GameManager

class ProjectLighthouse : Game() {

    private lateinit var game: GameManager
    private lateinit var gameScreen: GameScreen
    private lateinit var mainMenuScreen: MainMenuScreen
    private lateinit var battleScreen: BattleScreen

    override fun create() {
        game = GameManager()
        gameScreen = GameScreen(game)
        mainMenuScreen = MainMenuScreen(game)
        battleScreen = BattleScreen(game)
        Gdx.input.inputProcessor = game.inputHandler
        setScreen(gameScreen)
    }

    override fun render() {
        screen.render(Gdx.graphics.deltaTime)

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) setScreen(mainMenuScreen)
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) setScreen(gameScreen)
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) setScreen(battleScreen)
    }

    /*override fun dispose() {
        gameScreen.dispose()
    }*/
}