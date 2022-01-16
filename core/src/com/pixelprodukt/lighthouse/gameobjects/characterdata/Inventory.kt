package com.pixelprodukt.lighthouse.gameobjects.characterdata

import com.pixelprodukt.lighthouse.battle.enums.ItemType
import com.pixelprodukt.lighthouse.gameobjects.itemdata.Item
import com.pixelprodukt.lighthouse.system.Console

class Inventory {
    private val console = Console(Inventory::class.simpleName!!)

    val items = arrayOf<Item>(
        Item("Money", ItemType.MONEY, 0),
        Item("s. Health Potion", ItemType.HEALING_POTION_S, 0),
        Item("m. Health Potion", ItemType.HEALING_POTION_M, 0),
    )

    fun addItem(other: Item) {
        val item = items.find { item -> item.type == other.type } ?: throw Exception("Item not found in inventory")
        item.quantity += other.quantity
    }

    fun log() {
        items.forEach { item -> console.log("item: ${item.label} | quantity: ${item.quantity}") }
    }
}