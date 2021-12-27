package com.pixelprodukt.lighthouse.screens

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pixelprodukt.lighthouse.battle.enums.CharacterAction
import com.pixelprodukt.lighthouse.battle.model.*
import com.pixelprodukt.lighthouse.system.GameManager
import com.pixelprodukt.lighthouse.ui.MenuSelection
import com.pixelprodukt.lighthouse.ui.UiSelectionMenu
import com.pixelprodukt.lighthouse.ui.UiSimpleTextBox
import ktx.app.clearScreen
import ktx.graphics.use

class BattleScreen(private val context: GameManager) : Screen {

    private val simpleMessageMenu = UiSimpleTextBox(context.assetHandler, 2f, 174f, 252f, 16f)
    private val characterActionsSelectionMenu = UiSelectionMenu(context.assetHandler, 1, 2f, 2f, 86f, 48f)
    private val enemyCharacterSelectionMenu = UiSelectionMenu(context.assetHandler, 1, 90f, 90f, 86f, 48f)
    private val playerCharacterSelectionMenu =
        UiSelectionMenu(context.assetHandler, 1, 2f, 2f, 86f, 90f)
    private val itemSelectionMenu = UiSelectionMenu(context.assetHandler, 1, 90f, 2f, 128f, 48f)

    private var selectedPlayerAction: CharacterAction? = null
    private var currentSelectedPlayerModel: CharacterModel? = null
    private var currentSelectedEnemyModel: CharacterModel? = null
    private var selectedItem: Item? = null
    private var selectedTarget: CharacterModel? = null

    private val batch = SpriteBatch()
    private val camera = OrthographicCamera(1024f / 4f, 768f / 4f)
    private val battleHandler = context.battleHandler
    private lateinit var activeUiSelectionMenu: UiSelectionMenu

    private lateinit var battleModel: BattleModel
    private val enemyModel1 = CharacterModel("Slime 1", Attributes(12, 8, 8), Statistics(6, 14), mutableListOf())
    private val enemyModel2 = CharacterModel("Slime 2", Attributes(12, 8, 8), Statistics(6, 14), mutableListOf())

    init {
        init()
        initListeners()
    }

    fun init() {
        battleModel = battleHandler.startBattle(listOf(context.player.model), listOf(enemyModel1, enemyModel2))
        currentSelectedPlayerModel = battleHandler.getCurrentCharacterModel(battleModel)
        characterActionsSelectionMenu.addEntries(createCurrentPlayerMenu(battleModel))
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
            val enemy = selection.data as CharacterModel
            resolveTurn()
        }
        playerCharacterSelectionMenu.addSelectionListener { selection ->
            val player = selection.data as CharacterModel
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

    private fun createCurrentPlayerMenu(battleModel: BattleModel): List<MenuSelection> {
        val menu: MutableList<MenuSelection> = mutableListOf()
        battleModel.currentCharacterActions.forEach { action -> menu.add(MenuSelection(action.label, action)) }
        return menu.toList()
    }

    private fun createCurrentPlayerItemMenu(): List<MenuSelection> {
        val menu: MutableList<MenuSelection> = mutableListOf()
        currentSelectedPlayerModel?.inventory?.forEach { item ->
            if (item.isUsable) menu.add(MenuSelection(item.label, item, isDisabled = item.quantity == 0))
        }
        return menu.toList()
    }

    private fun createEnemySelectionMenu(): List<MenuSelection> {
        // TODO implementation
        return listOf(
            MenuSelection(enemyModel1.name, enemyModel1),
            MenuSelection(enemyModel2.name, enemyModel2)
        )
    }

    private fun handleInput() {
        if (context.inputHandler.isUpPressed) {
            context.inputHandler.isUpPressed = false
            activeUiSelectionMenu.setToEntryInPreviousRow()
        }
        if (context.inputHandler.isDownPressed) {
            context.inputHandler.isDownPressed = false
            activeUiSelectionMenu.setToEntryInNextRow()
        }

        if (context.inputHandler.isSpacePressed || context.inputHandler.isActionPressed) {
            context.inputHandler.isSpacePressed = false
            context.inputHandler.isActionPressed = false

            if (simpleMessageMenu.isFocussed) {
                simpleMessageMenu.nextMessage()
            } else if (activeUiSelectionMenu.isActive) {
                activeUiSelectionMenu.select()
            }
        }

        if (context.inputHandler.isBackPressed) {
            context.inputHandler.isBackPressed = false

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
            simpleMessageMenu.render(spriteBatch)
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