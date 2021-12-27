package com.pixelprodukt.lighthouse.battle.actions

import com.pixelprodukt.lighthouse.battle.model.CharacterModel

open abstract class BattleAction {
    val result = mutableListOf<String>("Nothing happened.")
    protected abstract fun execute()
}