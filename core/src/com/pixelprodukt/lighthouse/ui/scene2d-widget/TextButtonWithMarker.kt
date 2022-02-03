package com.pixelprodukt.lighthouse.ui.`scene2d-widget`

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.pixelprodukt.lighthouse.constants.Assets
import com.ray3k.stripe.FreeTypeSkin

class TextButtonWithMarker(text: String, skin: FreeTypeSkin) : TextButton(text, skin) {

    private val markerTexture = Texture(Assets.MENU_MARKER)

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        if (hasKeyboardFocus()) {
            batch!!.draw(markerTexture, x - markerTexture.width - 2, y - 1 + (markerTexture.height / 2))
        }
    }
}