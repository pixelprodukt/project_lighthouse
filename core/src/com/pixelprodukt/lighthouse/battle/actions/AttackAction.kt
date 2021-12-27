package com.pixelprodukt.lighthouse.battle.actions

import com.pixelprodukt.lighthouse.battle.model.CharacterModel

class AttackAction(
    private val combatant: CharacterModel,
    private val targets: List<CharacterModel>
    ) : BattleAction() {
    public override fun execute() {
        // TODO: CharacterModels need damage/weapon. For now, fixed value
        result.clear()
        targets.forEach { target ->
            target.statistics.health - 10
            result.add("${combatant.name} hit ${target.name} for ${10} damage!")
            result.add("This is a test!")
            result.add("This is also a test!")
            if (target.statistics.health <= 0) {
                // TODO: target.isDead = true
                result.add("${target.name} is dead.")
            }
        }
    }
}