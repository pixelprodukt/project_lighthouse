package com.pixelprodukt.lighthouse.gameobjects

import com.pixelprodukt.lighthouse.CharacterConfig
import com.pixelprodukt.lighthouse.interfaces.Interactable
import com.pixelprodukt.lighthouse.system.AnimationController
import com.pixelprodukt.lighthouse.system.Body

open abstract class NpcCharacter(
    config: CharacterConfig,
    animationController: AnimationController,
    private val isStationary: Boolean = false
) : Character(config, animationController), Interactable {

    override val sensor: Body = Body()
    override val listeners: MutableList<(Event: Any) -> Unit> = mutableListOf()

    //private var state = 2
    private var movingTime: Float = 0f
    private var boundsWidth: Float? = null
    private var boundsHeight: Float? = null

    init {
        actionDuration = 0.5f
    }
}