package com.pixelprodukt.lighthouse.gameobjects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.pixelprodukt.lighthouse.constants.Assets
import com.pixelprodukt.lighthouse.gameobjects.itemdata.Item
import com.pixelprodukt.lighthouse.interfaces.Interactable
import com.pixelprodukt.lighthouse.system.Body

class Chest(val loot: MutableList<Item> = mutableListOf()) : GameObject(), Interactable {

    override val sensor: Body = Body()
    override val listeners: MutableList<(Event: Any) -> Unit> = mutableListOf()

    private val closedTextureRegion: TextureRegion
    private val openedTextureRegion: TextureRegion
    private var isOpen: Boolean = false

    init {
        val texture = Texture(Assets.CHEST)
        closedTextureRegion = TextureRegion(texture, 0, 0, 16, 16)
        openedTextureRegion = TextureRegion(texture, 16, 0, 16, 16)
        region = closedTextureRegion
    }

    override fun interact() {
        if (isOpen) return
        listeners.forEach { callback -> callback(loot) }
        isOpen = true
        region = openedTextureRegion
    }
}