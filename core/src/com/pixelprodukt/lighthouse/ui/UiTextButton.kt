package com.pixelprodukt.lighthouse.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pixelprodukt.lighthouse.constants.Assets
import com.pixelprodukt.lighthouse.handler.AssetHandler

class UiTextButton(
    private val assetHandler: AssetHandler,
    private val x: Float,
    private val y: Float
) {
    private val menuMarker = assetHandler.assets.get<Texture>(Assets.MENU_MARKER)
    private val font = assetHandler.getFont()
    var label = ""
    var isSelected = false
    private val listeners = mutableListOf<() -> Unit>()

    fun addClickListener(listener: () -> Unit) {
        listeners.add(listener)
    }

    fun render(batch: SpriteBatch) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && isSelected) {
            listeners.forEach { listener -> listener.invoke() }
        }
        if (isSelected) batch.draw(menuMarker, x, y)
        font.color = Color(48f / 255f, 104f / 255f, 80f / 255f, 1f)
        font.draw(batch, label, x + 6, y)
    }
}