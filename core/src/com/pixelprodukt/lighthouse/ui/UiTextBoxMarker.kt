package com.pixelprodukt.lighthouse.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pixelprodukt.lighthouse.constants.Assets

class UiTextBoxMarker(x: Float = 0f, y: Float = 0f) : UiView(x, y) {

    private val textBoxMarkerFull = Texture(Assets.TEXTBOX_ARROW_FULL)
    private val textBoxMarkerEmpty = Texture(Assets.TEXTBOX_ARROW_EMPTY)
    private var currentTextMarker = textBoxMarkerEmpty
    private var stateTime = 0f
    private val blinkTime = 0.6f

    override var width: Float = 0f
        get() = textBoxMarkerFull.width.toFloat()

    override var height: Float = 0f
        get() = textBoxMarkerFull.height.toFloat()

    override fun renderImplementation(batch: SpriteBatch) {
        stateTime += Gdx.graphics.deltaTime
        if (stateTime > blinkTime) {
            if (currentTextMarker === textBoxMarkerFull) {
                currentTextMarker = textBoxMarkerEmpty
            } else {
                currentTextMarker = textBoxMarkerFull
            }
            stateTime = 0f
        }
        batch.draw(currentTextMarker, x, y)
    }
}