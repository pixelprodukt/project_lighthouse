package com.pixelprodukt.lighthouse.system

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.pixelprodukt.lighthouse.ProjectLighthouse
import com.pixelprodukt.lighthouse.constants.Assets
import com.pixelprodukt.lighthouse.enums.GameScreen
import com.pixelprodukt.lighthouse.gameobjects.Character
import com.pixelprodukt.lighthouse.gameobjects.characterdata.Attributes
import com.pixelprodukt.lighthouse.gameobjects.characterdata.Statistics
import com.pixelprodukt.lighthouse.gameobjects.CombatCharacter
import com.pixelprodukt.lighthouse.gameobjects.SimpleNpcCharacter
import com.pixelprodukt.lighthouse.gameobjects.characterdata.Inventory
import com.pixelprodukt.lighthouse.handler.*
import com.ray3k.stripe.FreeTypeSkin
import ktx.app.KtxScreen

/**
 * Class for holding gamestate as well as services or
 * "global" variables or in general as glue code
 */
class GameManager(private val game: ProjectLighthouse) {

    /*val eventHandler = EventHandler()*/
    val assetHandler = AssetHandler()
    val inputHandler = InputHandler()
    val animationFactory: AnimationFactory = AnimationFactory()

    val fontskin = FreeTypeSkin(Gdx.files.internal("skin/lighthouse.json"))

    val player = Character("Dougan", animationFactory.createAnimationController(assetHandler, Assets.PLAYER)
    ).apply {
        x = 16
        y = 16
    }

    /*val testNpc = SimpleNpcCharacter(
        "Nadja",
        animationFactory.createAnimationController(assetHandler, Assets.GIRL_01),
        mutableListOf(
            mutableListOf("Hey there, cutie!", "Would you like to go on a date with me?", "No? Sheesh, are you boring.", "Fucktard!"),
            mutableListOf("I'm from the battlestar woken.", "I will destroy you! Beep!"),
            mutableListOf("Have you heard of the nameless fear that dwells far in the east?")
        )
    ).apply {
        body.xy(180f, 180f)
        body.size(12f, 8f)
        transform.offset.set(6f, 8f)
        sensor.size(24f, 24f)
    }*/

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