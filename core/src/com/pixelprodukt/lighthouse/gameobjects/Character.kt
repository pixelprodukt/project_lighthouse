package com.pixelprodukt.lighthouse.gameobjects

import com.badlogic.gdx.Gdx
import com.pixelprodukt.lighthouse.*
import com.pixelprodukt.lighthouse.map.GameMap
import com.pixelprodukt.lighthouse.system.AnimationController
import com.pixelprodukt.lighthouse.system.Direction
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timerTask

open class Character(config: CharacterConfig, private val animationController: AnimationController) : GameObject(config) {

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

    private fun updatePosition() {
        when (direction) {
            Direction.UP -> y += 1 * speed
            Direction.DOWN -> y += -1 * speed
            Direction.LEFT -> x += -1 * speed
            Direction.RIGHT -> x += 1 * speed
        }
        movingProgressRemaining -= 1 * speed

        if (movingProgressRemaining == 0.0f) {
            EventHandler.dispatchEvent("personWalkingComplete", id)
        }
    }

    fun startBehaviour(state: UpdateState, behaviour: Behaviour) {
        direction = behaviour.direction
        if (behaviour.type == BehaviourType.WALK) {
            if (state.map.isSpaceTaken(x, y, direction!!)) {
                if (behaviour.retry) {
                    Timer().schedule(timerTask { startBehaviour(state, behaviour) }, 1000L)
                }
                return
            }
            state.map.moveWall(this.x, this.y, direction!!)
            this.movingProgressRemaining = 16.0f
            changeAnimationControllerState()
        }

        if (behaviour.type == BehaviourType.IDLE) {
            Timer().schedule(timerTask { EventHandler.dispatchEvent("personIdleComplete", id) }, behaviour.time)
        }
    }

    override fun update(state: UpdateState) {
        if (isActive) {
            super.update(state)
            if (movingProgressRemaining > 0.0f) {
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