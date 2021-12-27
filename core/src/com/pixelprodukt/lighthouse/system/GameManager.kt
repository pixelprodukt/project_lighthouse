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
import com.pixelprodukt.lighthouse.gameobjects.characterdata.Inventory
import com.pixelprodukt.lighthouse.handler.*

/**
 * Class for holding gamestate as well as services or
 * "global" variables or in general as glue code
 */
class GameManager {

    /*val eventHandler = EventHandler()*/
    val assetHandler = AssetHandler()
    val inputHandler = InputHandler()
    val animationFactory: AnimationFactory = AnimationFactory()

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
    )
        .apply {
            body.position.set(64f, 64f)
            body.offset.set(-6f, -8f)
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

    val mapHandler = MapHandler(player).apply { initMaps() }

    val batch = SpriteBatch()
    val shapeRenderer = ShapeRenderer();
    val camera = OrthographicCamera(1024f / 4f, 768f / 4f) //renderingSystem.camera

    /**
     * Game Config Constants
     */
    var masterVolume = 1.0f
}