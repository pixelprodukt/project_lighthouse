package com.pixelprodukt.lighthouse.gameobjects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.pixelprodukt.lighthouse.constants.Assets

class Chest : GameObject() {

    private val closedTextureRegion: TextureRegion
    private val openedTextureRegion: TextureRegion

    init {
        val texture = Texture(Assets.CHEST)
        closedTextureRegion = TextureRegion(texture, 0, 0, 16, 16)
        openedTextureRegion = TextureRegion(texture, 16, 0, 16, 16)
        region = closedTextureRegion
    }
}