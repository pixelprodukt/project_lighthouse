package com.pixelprodukt.lighthouse.gameobjects

import com.badlogic.gdx.Gdx
import com.pixelprodukt.lighthouse.*
import com.pixelprodukt.lighthouse.map.GameMap
import com.pixelprodukt.lighthouse.system.AnimationController
import com.pixelprodukt.lighthouse.system.Direction

open class Character(
    config: CharacterConfig,
    private val animationController: AnimationController,
    private val behaviourLoop: List<BehaviourConfig> = config.behaviourLoop
    ) : GameObject(config) {

    var speed = 0.8f
    val isPlayerControlled = config.isPlayerControlled
    private var isMoving = false
    private var currentBehaviour: Behaviour? = null
    var behaviourLoopIndex = 0

    private fun changeAnimationControllerState() {
        if (isMoving) {
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

    private fun startAction(map: GameMap, behaviourConfig: BehaviourConfig) {

        this.direction = behaviourConfig.direction

        if (behaviourConfig.type == BehaviourType.WALK) {
            if (map.isSpaceTaken(x, y, behaviourConfig.direction)) {
                return
            }
            val walkBehaviour = WalkBehaviour(this, behaviourConfig.direction, GRIDSIZE, speed)
            walkBehaviour.begin()
            currentBehaviour = walkBehaviour
            isMoving = true
            val nextPos = nextPosition(x, y, behaviourConfig.direction)
            wall.x = nextPos.x
            wall.y = nextPos.y
        }

        if (behaviourConfig.type == BehaviourType.IDLE) {
            val idleBehaviour = IdleBehaviour(this, behaviourConfig.duration!!)
            currentBehaviour = idleBehaviour
            isMoving = false
        }
    }

    override fun update(delta: Float, state: UpdateState) {

        if (currentBehaviour?.isFinished == true) {
            currentBehaviour = null
            isMoving = false
        }

        if (currentBehaviour == null) {
            if (isPlayerControlled && state.direction != null) {
                startAction(state.map, BehaviourConfig(state.behaviourType, state.direction))
            } else if (behaviourLoop.isNotEmpty()) {
                if (behaviourLoopIndex > behaviourLoop.size - 1) {
                    behaviourLoopIndex = 0
                }
                startAction(state.map, behaviourLoop[behaviourLoopIndex])
            }
        }

        currentBehaviour?.update(delta)

        changeAnimationControllerState()
        updateAnimationControllerAndRegion()
    }
}