package com.pixelprodukt.lighthouse.gameobjects

import com.pixelprodukt.lighthouse.system.AnimationController

class SimpleNpcCharacter(
    name: String,
    animationController: AnimationController,
    private val messages: MutableList<MutableList<String>> = mutableListOf(),
    isStationary: Boolean = false
) : NpcCharacter(name, animationController, isStationary) {

    override fun interact() {
        listeners.forEach { callback -> callback(messages.random()) }
    }
}