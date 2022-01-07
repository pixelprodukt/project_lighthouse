package com.pixelprodukt.lighthouse.system

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.pixelprodukt.lighthouse.battle.enums.ItemType
import com.pixelprodukt.lighthouse.constants.Assets
import com.pixelprodukt.lighthouse.gameobjects.characterdata.Attributes
import com.pixelprodukt.lighthouse.gameobjects.characterdata.Item
import com.pixelprodukt.lighthouse.gameobjects.characterdata.Statistics
import com.pixelprodukt.lighthouse.gameobjects.CombatCharacter
import com.pixelprodukt.lighthouse.gameobjects.SimpleNpcCharacter
import com.pixelprodukt.lighthouse.gameobjects.characterdata.Inventory
import com.pixelprodukt.lighthouse.handler.*
import kotlinx.serialization.json.Json

/**
 * Class for holding gamestate as well as services or
 * "global" variables or in general as glue code
 */
class GameManager {

    /*val eventHandler = EventHandler()*/
    val assetHandler = AssetHandler()
    val inputHandler = InputHandler()
    val animationFactory: AnimationFactory = AnimationFactory()

    val json = Json

    val inventory = Inventory().apply {
        items.addAll(
            mutableListOf<Item>(
                Item("S. Heal. Pot.", ItemType.HEALING_POTION_S, true, false, false, false, false, 3, 4, 8),
                Item("M. Heal. Pot.", ItemType.HEALING_POTION_M, true, false, false, false, false, 0, 6, 12),
                Item("S. Bomb", ItemType.BOMB_S, true, false, true, false, false, 5, 8, 14),
                Item("Money", ItemType.MONEY, false, false, false, false, false, 200, 0, 0)
            )
        )
    }

    val player = CombatCharacter(
        Statistics(20, 10),
        Attributes(12, 14, 10),
        inventory,
        "Dougan",
        animationFactory.createAnimationController(assetHandler, Assets.PLAYER)
    ).apply {
        body.xy(60f, 280f)
        /*body.xy(
            (48f + (16f / 2)) - (12f / 2),
            (128f + (16f / 2)) - (8f / 2)
        )*/ // this is just to center the body in the middle of the tile
        body.size(12f, 8f)
        //body.offset.set(0f, 0f)
        transform.offset.set(6f, 8f)
    }

    val testNpc = SimpleNpcCharacter(
        "Nadja",
        animationFactory.createAnimationController(assetHandler, Assets.GIRL_01),
        mutableListOf(
            mutableListOf("Hey there, cutie!", "Would you like to go on a date with me?", "No? Sheesh, are you boring.", "Fucktard!"),
            mutableListOf("I'm from the battlestar woken.", "I will destroy you! Beep!"),
            mutableListOf("Have you heard of the nameless fear that dwells far in the east?")
        )
    ).apply {
        body.xy(180f, 180f) // this is just to center the body in the middle of the tile
        body.size(12f, 8f)
        transform.offset.set(6f, 8f)
        sensor.size(24f, 24f)
        //sensor.position.set((body.position.x + (body.size.x / 2)) - sensor.size.x / 2, (body.position.y + (body.size.y / 2)) - sensor.size.y / 2)
    }

    val enemy1 = CombatCharacter(
        Statistics(6, 14),
        Attributes(12, 8, 8),
        Inventory(),
        "Slime 1",
        animationFactory.createAnimationController(assetHandler, Assets.PLAYER)
    )
    val enemy2 = CombatCharacter(
        Statistics(6, 14),
        Attributes(12, 8, 8),
        Inventory(),
        "Slime 2",
        animationFactory.createAnimationController(assetHandler, Assets.PLAYER)
    )

    val mapHandler = MapHandler(this).apply { initMaps() }

    /**
     * Game Config Constants
     */
    var masterVolume = 1.0f
}