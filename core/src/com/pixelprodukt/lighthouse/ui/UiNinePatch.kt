package com.pixelprodukt.lighthouse.ui

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.pixelprodukt.lighthouse.constants.Assets

class UiNinePatch(
    x: Float = 0f,
    y: Float = 0f,
    override var width: Float = 0f,
    override var height: Float = 0f
) : UiView(x, y) {

    constructor(width: Float = 0f, height: Float = 0f) : this(0f, 0f, width, height)

    private val texture = Texture(Assets.TEXTBOX_PATCH)
    private val ninePatch = NinePatch(texture, 4, 4, 4, 4)
    private val ninePatchDrawable = NinePatchDrawable(ninePatch)

    override fun renderImplementation(batch: SpriteBatch) {
        ninePatchDrawable.draw(batch, x, y, width, height)
    }
}