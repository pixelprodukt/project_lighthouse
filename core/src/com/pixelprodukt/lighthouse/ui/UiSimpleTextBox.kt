package com.pixelprodukt.lighthouse.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pixelprodukt.lighthouse.constants.Assets
import com.pixelprodukt.lighthouse.handler.AssetHandler

class UiSimpleTextBox(
    private val assetHandler: AssetHandler,
    private val x: Float,
    private val y: Float,
    private val width: Float,
    private val height: Float
) : UiBoxDrawable(assetHandler, x, y, width, height) {

    private val textBoxMarkerFull = assetHandler.assets.get<Texture>(Assets.TEXTBOX_ARROW_FULL)
    private val textBoxMArkerEmpty = assetHandler.assets.get<Texture>(Assets.TEXTBOX_ARROW_EMPTY)

    private var messages = mutableListOf<String>()
    private var currentTextMarker = textBoxMArkerEmpty
    private var stateTime = 0f
    private val blinkTime = 0.6f
    var isFocussed = false

    fun addMessages(newMessages: MutableList<String>) {
        this.messages.addAll(newMessages)
    }

    fun nextMessage() {
        if (messages.size >= 1) {
            messages.removeFirst()
        } else {
            isFocussed = false
        }
    }

    fun blinkTextBoxMarker() {
        if (stateTime > blinkTime) {
            if (currentTextMarker === textBoxMarkerFull) {
                currentTextMarker = textBoxMArkerEmpty
            } else {
                currentTextMarker = textBoxMarkerFull
            }
            stateTime = 0f
        }
    }

    override fun render(batch: SpriteBatch) {
        stateTime += Gdx.graphics.deltaTime
        blinkTextBoxMarker()
        super.render(batch)
        if (messages.size > 0) {
            font.draw(batch, messages[0], x + 5, y + 11)
            batch.draw(currentTextMarker, x + width - 12, y + 5)
        }
    }
}