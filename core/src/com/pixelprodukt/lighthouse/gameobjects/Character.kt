package com.pixelprodukt.lighthouse.gameobjects

import com.badlogic.gdx.Gdx
import com.pixelprodukt.lighthouse.Behaviour
import com.pixelprodukt.lighthouse.BehaviourType
import com.pixelprodukt.lighthouse.CharacterConfig
import com.pixelprodukt.lighthouse.UpdateState
import com.pixelprodukt.lighthouse.map.GameMap
import com.pixelprodukt.lighthouse.system.AnimationController
import com.pixelprodukt.lighthouse.system.Direction

open class Character(config: CharacterConfig, private val animationController: AnimationController) : GameObject(config) {

    private var direction: Direction? = Direction.DOWN
    private var movingProgressRemaining = 0.0f

    var speed = 1.0f
    val isPlayerControlled = config.isPlayerControlled

    private fun changeAnimationControllerState() {
        if (movingProgressRemaining > 0) {
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

    override fun mount(map: GameMap) {
        super.mount(map)
        if (behaviourLoop.size > 0) {
            playerBehaviourLoop(map)
        }
    }

    private fun updatePosition() {
        when (direction) {
            Direction.UP -> y += 1 * speed
            Direction.DOWN -> y += -1 * speed
            Direction.LEFT -> x += -1 * speed
            Direction.RIGHT -> x += 1 * speed
        }
        movingProgressRemaining -= 1 * speed
    }

    fun playerBehaviourLoop(map: GameMap) {
        startBehaviour(UpdateState(null, map = map), behaviourLoop[behaviourLoopIndex])
        behaviourLoopIndex++

        if (behaviourLoopIndex == behaviourLoop.size) {
            behaviourLoopIndex = 0
        }
    }

    fun startBehaviour(state: UpdateState, behaviour: Behaviour) {
        direction = behaviour.direction
        if (behaviour.type == BehaviourType.WALK) {
            if (state.map.isSpaceTaken(x, y, direction!!)) {
                state.map.moveWall(this.x, this.y, direction!!)
                this.movingProgressRemaining = 16.0f
            }
        }
    }

    override fun update(state: UpdateState) {
        if (isActive) {
            super.update(state)

            if (movingProgressRemaining > 0) {
                updatePosition();
            } else {
                if (behaviourLoop.size > 0 && movingProgressRemaining == 0.0f) {
                    playerBehaviourLoop(state.map)
                }
                if (isPlayerControlled && state.direction != null) {
                    startBehaviour(state, Behaviour(BehaviourType.WALK, state.direction))
                }
                changeAnimationControllerState()
            }
            updateAnimationControllerAndRegion()
        }
    }
}