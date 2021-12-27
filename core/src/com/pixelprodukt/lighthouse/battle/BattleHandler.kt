package com.pixelprodukt.lighthouse.battle

import com.pixelprodukt.lighthouse.battle.enums.CharacterAction
import com.pixelprodukt.lighthouse.battle.model.BattleModel
import com.pixelprodukt.lighthouse.battle.model.CharacterModel

class BattleHandler {

    fun startBattle(playerCharacterModels: List<CharacterModel>, enemyCharacterModels: List<CharacterModel>): BattleModel {

        val turnsPerRound = playerCharacterModels.size + enemyCharacterModels.size - 1
        val currentTurn = 0
        val currentCharacter = 0

        return BattleModel(
            playerCharacterModels,
            enemyCharacterModels,
            currentTurn,
            true,
            createCurrentCharacterActions(playerCharacterModels[currentCharacter])
        )
    }

    fun attack(battleModel: BattleModel, characterSelection: List<CharacterModel>) {

    }

    fun executeTurn() {
        /**
         *
         */
    }

    fun getCurrentCharacterModel(model: BattleModel): CharacterModel {
        return if (model.currentTurn <= model.playerCharacterModels.size - 1) {
            model.playerCharacterModels[model.currentTurn]
        } else {
            model.enemyCharacterModels[model.currentTurn]
        }
    }

    fun isCharacterDead(character: CharacterModel): Boolean {
        return character.statistics.health <= 0
    }

    private fun setNextTurn(model: BattleModel) {
        val turnsPerRound = model.playerCharacterModels.size + model.enemyCharacterModels.size - 1
        model.currentTurn++

        if (model.currentTurn > turnsPerRound) {
            model.currentTurn = 0
        }
        model.isPlayerTurn = model.currentTurn <= model.playerCharacterModels.size - 1

        if (isCharacterDead(getCurrentCharacterModel(model))) setNextTurn(model)
    }

    private fun getDeadCharacterCount(model: BattleModel) {

    }

    private fun createCurrentCharacterActions(character: CharacterModel): List<CharacterAction> {
        return listOf(CharacterAction.ATTACK, CharacterAction.USE_ITEM, CharacterAction.WAIT, CharacterAction.RETREAT)
    }
}