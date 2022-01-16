package com.pixelprodukt.lighthouse.gameobjects

import com.badlogic.gdx.Gdx
import com.pixelprodukt.lighthouse.system.AnimationController
import com.pixelprodukt.lighthouse.system.Direction

open abstract class Character(val name: String, private val animationController: AnimationController) : GameObject() {

    protected var direction: Direction = Direction.DOWN
    private val isMoving: Boolean
        get() {
            return body.velocity.x != 0f || body.velocity.y != 0f
        }

    var speed = 1.0f

    private fun setDirection() {
        if (body.velocity.y > 0f) direction = Direction.UP
        if (body.velocity.y < 0f) direction = Direction.DOWN
        if (body.velocity.x > 0f) direction = Direction.RIGHT
        if (body.velocity.x < 0f) direction = Direction.LEFT
    }

    private fun changeAnimationControllerState() {
        if (isMoving) {
            animationController.state = when (direction) {
                Direction.UP -> AnimationController.MOVE_UP
                Direction.DOWN -> AnimationController.MOVE_DOWN
                Direction.LEFT -> AnimationController.MOVE_LEFT
                Direction.RIGHT -> AnimationController.MOVE_RIGHT
            }
        } else {
            animationController.state = when (direction) {
                Direction.UP -> AnimationController.IDLE_UP
                Direction.DOWN -> AnimationController.IDLE_DOWN
                Direction.LEFT -> AnimationController.IDLE_LEFT
                Direction.RIGHT -> AnimationController.IDLE_RIGHT
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

    override fun update() {
        if (isActive) {
            super.update()
            setDirection()
            changeAnimationControllerState()
            updateAnimationControllerAndRegion()
        }
    }
}