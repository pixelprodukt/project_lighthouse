package com.pixelprodukt.lighthouse.screens

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pixelprodukt.lighthouse.enums.CharacterScreenState
import com.pixelprodukt.lighthouse.handler.MessageHandler
import com.pixelprodukt.lighthouse.system.Console
import com.pixelprodukt.lighthouse.system.GameManager
import com.pixelprodukt.lighthouse.ui.*
import ktx.app.clearScreen
import ktx.graphics.use

class CharacterScreen(private val game: GameManager) : Screen {

    private val console = Console(CharacterScreen::class.simpleName!!)

    private val batch = SpriteBatch()
    private val camera = OrthographicCamera(1024f / 4f, 768f / 4f)

    private val simpleTextBox = SimpleTextBox(248f, 26f)

    private val uiContainer = UiContainer(0f, 0f, 256f, 192f)
    private val uiNinePatchSideMenu = UiNinePatch(76f, 154f)
    private val uiNinePatchStatsMenu = UiNinePatch(168f, 154f)
    private val uiNinePatchAttributesMenu = UiNinePatch(168f, 154f)
    private val uiNinePatchInventoryMenu = UiNinePatch(168f, 154f)

    private val statsMenuEntry = UiText("Stats")
    private val attributesMenuEntry = UiText("Attrib.")
    private val inventoryMenuEntry = UiText("Invent.")

    private val selectionMarker = UiSelectionMarker()

    private val menuEntries = arrayOf(statsMenuEntry, attributesMenuEntry, inventoryMenuEntry)
    private var selectedIndex = 0

    private var state: CharacterScreenState = CharacterScreenState.SIDE_MENU

    init {
        simpleTextBox.centerOn(uiContainer)
        uiNinePatchSideMenu.alignToTopLeftOf(uiContainer, 4f)
        uiNinePatchStatsMenu.alignToTopRightSideOf(uiNinePatchSideMenu, 4f)
        uiNinePatchAttributesMenu.alignToTopRightSideOf(uiNinePatchSideMenu, 4f)
        uiNinePatchInventoryMenu.alignToTopRightSideOf(uiNinePatchSideMenu, 4f)
        statsMenuEntry.alignToTopLeftOf(uiNinePatchSideMenu, 12f)
        attributesMenuEntry.alignToBottomSideOf(statsMenuEntry, 4f)
        inventoryMenuEntry.alignToBottomSideOf(attributesMenuEntry, 4f)
        setUiViewOfSelectedMenuEntry()
        uiContainer.addChild(simpleTextBox)
        uiContainer.addChild(uiNinePatchSideMenu)
        uiContainer.addChild(uiNinePatchStatsMenu)
        uiContainer.addChild(uiNinePatchAttributesMenu)
        uiContainer.addChild(uiNinePatchInventoryMenu)
        uiContainer.addChild(statsMenuEntry)
        uiContainer.addChild(attributesMenuEntry)
        uiContainer.addChild(inventoryMenuEntry)
        uiContainer.addChild(selectionMarker)
        uiNinePatchStatsMenu.isVisible = false
        uiNinePatchAttributesMenu.isVisible = false
        uiNinePatchInventoryMenu.isVisible = false
        simpleTextBox.onClose { state = CharacterScreenState.CONTEXT_MENU }
    }

    private fun setSelectionMarkerNext() {
        if (selectedIndex < menuEntries.size - 1) {
            selectedIndex++
        } else {
            selectedIndex = 0
        }
        setUiViewOfSelectedMenuEntry()
    }

    private fun setSelectionMarkerPrevious() {
        if (selectedIndex <= 0) {
            selectedIndex = menuEntries.size - 1
        } else {
            selectedIndex--
        }
        setUiViewOfSelectedMenuEntry()
    }

    private fun setUiViewOfSelectedMenuEntry() {
        selectionMarker.centerOnYOf(menuEntries[selectedIndex])
        selectionMarker.alignToLeftSideOf(menuEntries[selectedIndex], 3f)
    }

    override fun render(delta: Float) {
        clearScreen(8f / 255f, 24f / 255f, 32f / 255f, 1f)
        camera.position.set(256f / 2, 192f / 2, 0f)
        camera.update()
        batch.projectionMatrix = camera.combined;

        handleInput()

        batch.use { spriteBatch ->
            uiContainer.render(spriteBatch)
        }
    }

    override fun show() {
        MessageHandler.addMessageListener { messages -> console.log(messages.toString()) }
    }

    override fun hide() {
        MessageHandler.removeAllListeners()
    }

    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun dispose() {}

    private fun handleInput() {
        if (state === CharacterScreenState.SIDE_MENU) {
            if (game.inputHandler.isUpPressed) {
                game.inputHandler.isUpPressed = false
                setSelectionMarkerPrevious()
            }
            if (game.inputHandler.isDownPressed) {
                game.inputHandler.isDownPressed = false
                setSelectionMarkerNext()
            }
            if (game.inputHandler.isSpacePressed || game.inputHandler.isActionPressed) {
                game.inputHandler.isSpacePressed = false
                game.inputHandler.isActionPressed = false

                uiNinePatchStatsMenu.isVisible = menuEntries[selectedIndex] === statsMenuEntry
                uiNinePatchAttributesMenu.isVisible = menuEntries[selectedIndex] === attributesMenuEntry
                uiNinePatchInventoryMenu.isVisible = menuEntries[selectedIndex] === inventoryMenuEntry
                state = CharacterScreenState.CONTEXT_MENU

                if (uiNinePatchInventoryMenu.isVisible) {
                    game.player.inventory.items.forEach { item ->
                        uiContainer.addChild(UiText(item.label))
                    }
                }
            }
        } else if (state === CharacterScreenState.CONTEXT_MENU) {
            if (game.inputHandler.isBackPressed) {
                game.inputHandler.isBackPressed = false
                state = CharacterScreenState.SIDE_MENU
                uiNinePatchStatsMenu.isVisible = false
                uiNinePatchAttributesMenu.isVisible = false
                uiNinePatchInventoryMenu.isVisible = false
            }
        } else if (state === CharacterScreenState.TEXTBOX_INTERACTION) {
            if (game.inputHandler.isSpacePressed || game.inputHandler.isActionPressed) {
                game.inputHandler.isSpacePressed = false
                game.inputHandler.isActionPressed = false
                simpleTextBox.nextMessage()
            }
        }




        if (game.inputHandler.isBackPressed) {
            game.inputHandler.isBackPressed = false
        }
    }
}