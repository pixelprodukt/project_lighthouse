package com.pixelprodukt.lighthouse.gameobjects.itemdata

import com.pixelprodukt.lighthouse.battle.enums.ItemType
import com.pixelprodukt.lighthouse.gameobjects.CombatCharacter
import com.pixelprodukt.lighthouse.handler.MessageHandler
import kotlin.random.Random

class ItemData(
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

open class Item(var label: String, var type: ItemType, var quantity: Int)

interface Usable {
    val minValue: Int
    val maxValue: Int
    fun use(target: CombatCharacter)
}

interface Equipable {
    fun equip()
    fun unequip()
}

enum class BodySlots {
    ARMOR,
    WEAPON,
    ACCESSORY_ONE,
    ACCESSORY_TWO
}

class HealthPotionSmall(quantity: Int) : Item("sm. Health Potion", ItemType.HEALING_POTION_S, quantity), Usable {
    override val minValue: Int = 8
    override val maxValue: Int = 12
    override fun use(target: CombatCharacter) {
        val healingValue = Random.nextInt(minValue, maxValue + 1)
        target.statistics.health += healingValue
        MessageHandler.publishMessages(mutableListOf("${target.name} was healed by $healingValue"))
    }
}