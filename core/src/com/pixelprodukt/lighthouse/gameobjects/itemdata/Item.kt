package com.pixelprodukt.lighthouse.gameobjects.itemdata

import com.pixelprodukt.lighthouse.enums.ItemType
import com.pixelprodukt.lighthouse.gameobjects.CombatCharacter
import com.pixelprodukt.lighthouse.handler.MessageHandler
import kotlin.random.Random

interface Usable {
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

open class Item(var label: String, var type: ItemType, var quantity: Int)

class HealingItem(label: String, type: ItemType, quantity: Int = 0) : Item(label, type, quantity), Usable {
    val minValue: Int = 8
    val maxValue: Int = 12
    override fun use(target: CombatCharacter) {
        val healingValue = Random.nextInt(minValue, maxValue + 1)
        target.statistics.health += healingValue
        MessageHandler.publishMessages(mutableListOf("df was healed by $healingValue"))
    }
}