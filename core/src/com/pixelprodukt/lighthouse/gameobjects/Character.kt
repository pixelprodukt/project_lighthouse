package com.pixelprodukt.lighthouse.gameobjects

import com.badlogic.gdx.Gdx
import com.pixelprodukt.lighthouse.system.AnimationController
import com.pixelprodukt.lighthouse.system.Direction

open class Character(val name: String, private val animationController: AnimationController) : GameObject() {

    private var direction: Direction? = Direction.DOWN
    private val isMoving: Boolean get() = movingProgressRemaining > 0

    var speed = 1
    var isPlayerControlled = false
    private var movingProgressRemaining = 0

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

    private fun updatePosition() {
        when (direction) {
            Direction.UP -> y += 1 * speed
            Direction.DOWN -> y += -1 * speed
            Direction.LEFT -> x += -1 * speed
            Direction.RIGHT -> x += 1 * speed
        }
        movingProgressRemaining -= 1 * speed

        if (movingProgressRemaining == 0) {
            /*Utils.emitEvent("PersonWalkingComplete", {
                whoId: this.id
            })*/
        }
    }

    private fun startBehaviour(state: UpdateState, behaviour: Behaviour) {
        direction = behaviour.direction
        if (behaviour.type == BehaviourType.WALK) {
            /*if (state.map.isSpaceTaken(this.x, this.y, this.direction)) {
                return;
            }
            state.map.moveWall(this.x, this.y, this.direction);*/
            this.movingProgressRemaining = 16
        }
    }

    override fun update(state: UpdateState) {
        if (isActive) {
            super.update(state)
            if (this.movingProgressRemaining > 0) {
                updatePosition();
            } else {
                if (isPlayerControlled && state.direction != null) {
                    startBehaviour(state, Behaviour(BehaviourType.WALK, state.direction))
                }
                changeAnimationControllerState()
            }
            updateAnimationControllerAndRegion()
        }
    }
}