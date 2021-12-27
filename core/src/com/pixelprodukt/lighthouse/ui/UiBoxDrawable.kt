package com.pixelprodukt.lighthouse.ui

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.pixelprodukt.lighthouse.constants.Assets
import com.pixelprodukt.lighthouse.handler.AssetHandler

open abstract class UiBoxDrawable(
    private val assetHandler: AssetHandler,
    private var x: Float,
    private var y: Float,
    private var width: Float,
    private var height: Float
) {
    private val textboxTexture = assetHandler.assets.get<Texture>(Assets.TEXTBOX_PATCH)
    private val textboxPatch = NinePatch(textboxTexture, 4, 4, 4, 4)
    private val textBoxDrawable = NinePatchDrawable(textboxPatch)
    protected val font = assetHandler.getFont()

    fun setPosition(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    fun setSize(width: Float, height: Float) {
        this.width = width
        this.height = height
    }

    open fun render(batch: SpriteBatch) {
        textBoxDrawable.draw(batch, x, y, width, height)
    }
}