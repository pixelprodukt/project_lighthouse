package com.pixelprodukt.lighthouse.screens

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pixelprodukt.lighthouse.battle.enums.CharacterAction
import com.pixelprodukt.lighthouse.gameobjects.CombatCharacter
import com.pixelprodukt.lighthouse.gameobjects.characterdata.Item
import com.pixelprodukt.lighthouse.system.GameManager
import com.pixelprodukt.lighthouse.ui.MenuSelection
import com.pixelprodukt.lighthouse.ui.UiSelectionMenu
import com.pixelprodukt.lighthouse.ui.SimpleTextBox
import ktx.app.clearScreen
import ktx.graphics.use

class BattleScreen(private val game: GameManager) : Screen {

    //private val simpleMessageMenu = SimpleTextBox(game.assetHandler, 2f, 174f, 252f, 16f)
    private val characterActionsSelectionMenu = UiSelectionMenu(game.assetHandler, 1, 2f, 2f, 86f, 48f)
    private val enemyCharacterSelectionMenu = UiSelectionMenu(game.assetHandler, 1, 90f, 90f, 86f, 48f)
    private val playerCharacterSelectionMenu =
        UiSelectionMenu(game.assetHandler, 1, 2f, 2f, 86f, 90f)
    private val itemSelectionMenu = UiSelectionMenu(game.assetHandler, 1, 90f, 2f, 128f, 48f)

    private var selectedPlayerAction: CharacterAction? = null
    private var player: CombatCharacter? = game.player
    private var currentSelectedEnemy: CombatCharacter? = null
    private var selectedItem: Item? = null
    private var selectedTarget: CombatCharacter? = null

    private val batch = SpriteBatch()
    private val camera = OrthographicCamera(1024f / 4f, 768f / 4f)
    private lateinit var activeUiSelectionMenu: UiSelectionMenu

    private val enemy1 = game.enemy1
    private val enemy2 = game.enemy2


    init {
        init()
        initListeners()
    }

    fun init() {
        characterActionsSelectionMenu.addEntries(createCurrentPlayerMenu())
        activeUiSelectionMenu = characterActionsSelectionMenu
        activeUiSelectionMenu.show()
        itemSelectionMenu.addEntries(createCurrentPlayerItemMenu())
        enemyCharacterSelectionMenu.addEntries(createEnemySelectionMenu())
    }

    private fun initListeners() {
        characterActionsSelectionMenu.addSelectionListener { selection ->
            val action = selection.data as CharacterAction
            when (action) {
                CharacterAction.ATTACK -> {
                    activeUiSelectionMenu = enemyCharacterSelectionMenu
                    enemyCharacterSelectionMenu.show()
                }
                CharacterAction.USE_ITEM -> {
                    activeUiSelectionMenu = itemSelectionMenu
                    itemSelectionMenu.show()
                }
            }
        }
        itemSelectionMenu.addSelectionListener { selection ->
            val item = selection.data as Item
            if (item.isDamageItem) {
                activeUiSelectionMenu = enemyCharacterSelectionMenu
                enemyCharacterSelectionMenu.show()
            } else {
                activeUiSelectionMenu = playerCharacterSelectionMenu
                playerCharacterSelectionMenu.show()
            }
        }
        enemyCharacterSelectionMenu.addSelectionListener { selection ->
            val enemy = selection.data as CombatCharacter
            resolveTurn()
        }
        playerCharacterSelectionMenu.addSelectionListener { selection ->
            val player = selection.data as CombatCharacter
            resolveTurn()
        }
    }

    private fun resolveTurn() {

    }

    private fun resetState() {
        selectedTarget = null
        selectedItem = null
    }

    private fun resetUi() {
        playerCharacterSelectionMenu.hide()
        enemyCharacterSelectionMenu.hide()
        itemSelectionMenu.hide()
        characterActionsSelectionMenu.isActive = true
        activeUiSelectionMenu = characterActionsSelectionMenu
    }

    private fun createCurrentPlayerMenu(): List<MenuSelection> {
        val menu: MutableList<MenuSelection> = mutableListOf()
        val actions = listOf(CharacterAction.ATTACK, CharacterAction.USE_ITEM, CharacterAction.WAIT, CharacterAction.RETREAT)
        actions.forEach { action -> menu.add(MenuSelection(action.label, action)) }
        return menu.toList()
    }

    private fun createCurrentPlayerItemMenu(): List<MenuSelection> {
        val menu: MutableList<MenuSelection> = mutableListOf()
        player?.inventory?.items?.forEach { item ->
            if (item.isUsable) menu.add(MenuSelection(item.label, item, isDisabled = item.quantity == 0))
        }
        return menu.toList()
    }

    private fun createEnemySelectionMenu(): List<MenuSelection> {
        // TODO implementation
        return listOf(
            MenuSelection(enemy1.name, enemy1),
            MenuSelection(enemy2.name, enemy2)
        )
    }

    private fun handleInput() {
        if (game.inputHandler.isUpPressed) {
            game.inputHandler.isUpPressed = false
            activeUiSelectionMenu.setToEntryInPreviousRow()
        }
        if (game.inputHandler.isDownPressed) {
            game.inputHandler.isDownPressed = false
            activeUiSelectionMenu.setToEntryInNextRow()
        }

        if (game.inputHandler.isSpacePressed || game.inputHandler.isActionPressed) {
            game.inputHandler.isSpacePressed = false
            game.inputHandler.isActionPressed = false

            /*if (simpleMessageMenu.isFocussed) {
                simpleMessageMenu.nextMessage()
            } else if (activeUiSelectionMenu.isActive) {
                activeUiSelectionMenu.select()
            }*/
        }

        if (game.inputHandler.isBackPressed) {
            game.inputHandler.isBackPressed = false

            resetState()
            resetUi()
        }
    }

    override fun show() {
        // TODO("Not yet implemented")
    }

    override fun render(delta: Float) {
        clearScreen(8f / 255f, 24f / 255f, 32f / 255f, 1f)
        camera.position.set(256f / 2, 192f / 2, 0f)
        camera.update()
        batch.projectionMatrix = camera.combined;

        handleInput()

        batch.use { spriteBatch ->
            //simpleMessageMenu.render(spriteBatch)
            characterActionsSelectionMenu.render(spriteBatch)
            itemSelectionMenu.render(spriteBatch)
            playerCharacterSelectionMenu.render(spriteBatch)
            enemyCharacterSelectionMenu.render(spriteBatch)
        }
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