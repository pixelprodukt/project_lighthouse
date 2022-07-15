package com.pixelprodukt.lighthouse.gameobjects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.pixelprodukt.lighthouse.*
import com.pixelprodukt.lighthouse.map.GameMap
import com.pixelprodukt.lighthouse.system.AnimationController
import com.pixelprodukt.lighthouse.system.Direction

open class Character(config: CharacterConfig, private val animationController: AnimationController) : GameObject(config) {

    var actionDuration = 0.3f
    val isPlayerControlled = config.isPlayerControlled
    var currentBehaviour: Behaviour? = null
    /*val behaviourLoop
    val behaviourLoopIndex
    val currentBehaviour*/

    private fun changeAnimationControllerState() {
        if (false) {
            animationController.state = when (direction) {
                Direction.UP -> AnimationController.MOVE_UP
                Direction.DOWN -> AnimationController.MOVE_DOWN
                Direction.LEFT -> AnimationController.MOVE_LEFT
                Direction.RIGHT -> AnimationController.MOVE_RIGHT
                else -> AnimationController.MOVE_DOWN
            }
        } else {
            animationController.state = when (direction) {
                Direction.UP -> AnimationController.IDLE_UP
                Direction.DOWN -> AnimationController.IDLE_DOWN
                Direction.LEFT -> AnimationController.IDLE_LEFT
                Direction.RIGHT -> AnimationController.IDLE_RIGHT
                else -> AnimationController.IDLE_DOWN
            }
        }
    }

    private fun updateAnimationControllerAndRegion() {
        animationController.time += Gdx.graphics.deltaTime
        region = animationController.animations[animationController.state]?.getKeyFrame(
            animationController.time,
            animationController.isLooping
        )!!
    }

    private fun updatePosition() {
        when (direction) {
            Direction.UP -> y += 1 * actionDuration
            Direction.DOWN -> y += -1 * actionDuration
            Direction.LEFT -> x += -1 * actionDuration
            Direction.RIGHT -> x += 1 * actionDuration
        }
    }

    fun startAction(map: GameMap, direction: Direction, behaviourType: BehaviourType) {
        this.direction = direction
        if (behaviourType == BehaviourType.WALK) {
            if (map.isSpaceTaken(x, y, direction)) {
                return
            }
            val walkBehaviour = WalkBehaviour(this, direction, GRIDSIZE, 0.5f)
            walkBehaviour.begin()
            currentBehaviour = walkBehaviour
            val nextPos = nextPosition(x, y, direction)
            wall.x = nextPos.x
            wall.y = nextPos.y
        }
    }

    override fun update(delta: Float, pressedKey: Direction?, map: GameMap) {
        if (currentBehaviour?.isFinished == true) {
            currentBehaviour = null
        }
        if (isPlayerControlled && pressedKey != null && currentBehaviour == null) {
            startAction(map, pressedKey, BehaviourType.WALK)
            println("here")
        }

        currentBehaviour?.update(delta)

        changeAnimationControllerState()
        updateAnimationControllerAndRegion()
    }
}