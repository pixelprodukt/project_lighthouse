package com.pixelprodukt.lighthouse.gameobjects.characterdata

import com.pixelprodukt.lighthouse.battle.enums.ItemType

data class Item(
    var label: String,
    var type: ItemType,
    var isUsable: Boolean,
    var isMultiTargetable: Boolean,
    var isDamageItem: Boolean,
    var isEquippable: Boolean,
    var isEquipped: Boolean,
    var quantity: Int,
    var minValue: Int?,
    var maxValue: Int?
)