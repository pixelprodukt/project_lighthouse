package com.pixelprodukt.lighthouse.battle.actions

open abstract class BattleAction {
    val result = mutableListOf<String>("Nothing happened.")
    protected abstract fun execute()
}