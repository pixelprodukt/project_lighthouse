package com.pixelprodukt.lighthouse.battle.model

import com.pixelprodukt.lighthouse.battle.enums.CharacterAction

data class BattleModel(
    val playerCharacterModels: List<CharacterModel>,
    val enemyCharacterModels: List<CharacterModel>,
    var currentTurn: Int = 0,
    var isPlayerTurn: Boolean,
    var currentCharacterActions: List<CharacterAction>,
    // var currentCharacterAbilities: List<String> // TODO: I still have to figure out, what abilities exactly are
)