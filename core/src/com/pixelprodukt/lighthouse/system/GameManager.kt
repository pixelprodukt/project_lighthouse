package com.pixelprodukt.lighthouse.system

import com.badlogic.gdx.Gdx
import com.pixelprodukt.lighthouse.ProjectLighthouse
import com.pixelprodukt.lighthouse.constants.Assets
import com.pixelprodukt.lighthouse.enums.GameScreen
import com.pixelprodukt.lighthouse.gameobjects.Character
import com.pixelprodukt.lighthouse.gameobjects.SimpleNpcCharacter
import com.pixelprodukt.lighthouse.getCharacterConfig
import com.pixelprodukt.lighthouse.handler.AssetHandler
import com.pixelprodukt.lighthouse.handler.InputHandler
import com.pixelprodukt.lighthouse.handler.MapHandler
import com.ray3k.stripe.FreeTypeSkin

/**
 * Class for holding gamestate as well as services or
 * "global" variables or in general as glue code
 */
class GameManager(private val game: ProjectLighthouse) {

    val assetHandler = AssetHandler()
    val inputHandler = InputHandler()
    val animationFactory: AnimationFactory = AnimationFactory()

    val fontskin = FreeTypeSkin(Gdx.files.internal("skin/lighthouse.json"))

    private val playerConfig = getCharacterConfig("player.json")
    val player = Character(playerConfig, animationFactory.createAnimationController(assetHandler, playerConfig.spritesheet))

    private val girlConfig = getCharacterConfig("testNpc.json")
    val testNpc = SimpleNpcCharacter(
        girlConfig,
        animationFactory.createAnimationController(assetHandler, Assets.GIRL_01),
        mutableListOf(
            mutableListOf("Hey there, cutie!", "Would you like to go on a date with me?", "No? Sheesh, are you boring.", "Fucktard!"),
            mutableListOf("I'm from the battlestar woken.", "I will destroy you! Beep!"),
            mutableListOf("Have you heard of the nameless fear that dwells far in the east?")
        )
    )

    val mapHandler = MapHandler(this).apply { initMaps() }

    /**
     * Game Config Constants
     */
    var masterVolume = 1.0f

    fun setScreen(gameScreenType: GameScreen) {
        when(gameScreenType) {
            GameScreen.MAIN -> game.screen = game.mainMenuScreen
            GameScreen.WORLD -> game.screen = game.worldMapScreen
            GameScreen.CHARACTER -> game.screen = game.testScreen
        }
    }
}