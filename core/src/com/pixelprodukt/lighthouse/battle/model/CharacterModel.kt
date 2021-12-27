package com.pixelprodukt.lighthouse.battle.model

data class CharacterModel(
    val name: String,
    val attributes: Attributes,
    val statistics: Statistics,
    var inventory: MutableList<Item>
)
