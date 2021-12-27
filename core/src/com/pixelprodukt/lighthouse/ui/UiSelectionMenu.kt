package com.pixelprodukt.lighthouse.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pixelprodukt.lighthouse.constants.Assets
import com.pixelprodukt.lighthouse.handler.AssetHandler

class UiSelectionMenu(
    private val assetHandler: AssetHandler,
    private val columnSize: Int,
    private val x: Float,
    private val y: Float,
    private val width: Float,
    private val height: Float,
) : UiBoxDrawable(assetHandler, x, y, width, height) {

    private val maxRowsOnScreen = 10
    private var currentRowIndex = 0
    private var currentColumnIndex = 0
    private val menuMarker = assetHandler.assets.get<Texture>(Assets.MENU_MARKER)
    private val columns = Array(columnSize) { mutableListOf<MenuSelection>() }
    private var isVisible = false
    var isActive = false
    var parent: UiSelectionMenu? = null
    private val listeners: MutableList<(MenuSelection) -> Unit> = mutableListOf()

    fun addEntries(entries: List<MenuSelection>) {
        // Todo: Handle several columns
        columns[0].clear()
        columns[0].addAll(entries)
    }

    fun addSelectionListener(listener: (selection: MenuSelection) -> Unit) {
        listeners.add(listener)
    }

    fun select() {
        val selectedEntry = columns[currentColumnIndex][currentRowIndex]
        listeners.forEach { listener -> listener.invoke(selectedEntry) }
    }

    fun setToEntryInNextRow() {
        currentRowIndex++
        if (currentRowIndex >= columns[currentColumnIndex].size) currentRowIndex = 0
    }

    fun setToEntryInPreviousRow() {
        currentRowIndex--
        if (currentRowIndex < 0) currentRowIndex = columns[currentColumnIndex].size - 1
    }

    fun setToEntryInNextColumn() {
        currentColumnIndex++
        if (currentColumnIndex >= columns.size) currentColumnIndex = 0
    }

    fun setToEntryInPreviousColumn() {
        currentColumnIndex--
        if (currentColumnIndex < 0) currentColumnIndex = columns.size - 1
    }

    fun show() {
        isVisible = true
        isActive = true
    }

    fun hide() {
        isVisible = false
        isActive = false
    }

    override fun render(batch: SpriteBatch) {
        if (isVisible) {
            super.render(batch)

            columns.forEachIndexed { colIndex, column ->

                column.forEachIndexed { index, entry ->

                    if (isActive && currentColumnIndex == colIndex && currentRowIndex == index) {
                        batch.draw(
                            menuMarker,
                            x + 6 + (6 * colIndex),
                            y + height - 6 - 6 - (10 * index)
                        )
                    }

                    font.color = Color(48f / 255f, 104f / 255f, 80f / 255f, 1f)
                    if (entry.isDisabled) font.color = Color(136f / 255f, 192f / 255f, 112f / 255f, 1f)
                    font.draw(
                        batch,
                        "${entry.label}",
                        x + 12 + (8 * colIndex),
                        y + height - 6 - (10 * index)
                    )
                }
            }
        }
    }
}