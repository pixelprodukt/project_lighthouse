package com.pixelprodukt.lighthouse.gameobjects

import com.pixelprodukt.lighthouse.CharacterConfig
import com.pixelprodukt.lighthouse.system.AnimationController

class SimpleNpcCharacter(
    config: CharacterConfig,
    animationController: AnimationController,
    private val messages: MutableList<MutableList<String>> = mutableListOf(),
    isStationary: Boolean = false
) : NpcCharacter(config, animationController, isStationary) {

    override fun interact() {
        listeners.forEach { callback -> callback(messages.random()) }
    }
}