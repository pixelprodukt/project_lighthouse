package com.pixelprodukt.lighthouse.gameobjects

import com.pixelprodukt.lighthouse.gameobjects.characterdata.Attributes
import com.pixelprodukt.lighthouse.gameobjects.characterdata.Inventory
import com.pixelprodukt.lighthouse.gameobjects.characterdata.Statistics
import com.pixelprodukt.lighthouse.system.AnimationController

class CombatCharacter(
    val statistics: Statistics,
    val attributes: Attributes,
    val inventory: Inventory,
    name: String,
    animationController: AnimationController
) : Character(name, animationController) {
}