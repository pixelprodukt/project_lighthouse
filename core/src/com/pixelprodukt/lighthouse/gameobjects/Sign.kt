package com.pixelprodukt.lighthouse.gameobjects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.pixelprodukt.lighthouse.GameObjectConfig
import com.pixelprodukt.lighthouse.constants.Assets
import com.pixelprodukt.lighthouse.interfaces.Interactable
import com.pixelprodukt.lighthouse.system.Body

class Sign(config: GameObjectConfig, var text: String = "") : GameObject(config), Interactable {

    override val sensor: Body = Body()
    override val listeners: MutableList<(Event: Any) -> Unit> = mutableListOf()

    init {
        val texture = Texture(Assets.SIGN)
        region = TextureRegion(texture, 0, 0, 16, 16)
    }

    override fun interact() {
        listeners.forEach { callback -> callback(text) }
    }
}