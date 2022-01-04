package com.pixelprodukt.lighthouse.gameobjects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.pixelprodukt.lighthouse.constants.Assets

class Sign : GameObject() {

    init {
        val texture = Texture(Assets.SIGN)
        region = TextureRegion(texture, 0, 0, 16, 16)
    }
}