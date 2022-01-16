package com.pixelprodukt.lighthouse.ui

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pixelprodukt.lighthouse.constants.Assets

class UiSelectionMarker(x: Float = 0f, y: Float = 0f) : UiView(x, y) {

    private val selectionMarker = Texture(Assets.MENU_MARKER)

    override var width: Float = 0f
        get() = selectionMarker.width.toFloat()

    override var height: Float = 0f
        get() = selectionMarker.height.toFloat()

    override fun renderImplementation(batch: SpriteBatch) {
        batch.draw(selectionMarker, x, y)
    }
}