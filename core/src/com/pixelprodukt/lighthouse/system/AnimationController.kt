package com.pixelprodukt.lighthouse.system

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion

class AnimationController() {

    val animations: HashMap<Int, Animation<TextureRegion>> = HashMap()
    var time: Float = 0.2f
    var isLooping: Boolean = true
    var state: Int? = null
        set(value) {
            // Prevent permanent setter calls in update when old state is the same as the new state
            if (field != value) {
                field = value
                time = 0.2f
            }
        }

    companion object {
        const val IDLE_UP = 0
        const val IDLE_DOWN = 1
        const val IDLE_LEFT = 2
        const val IDLE_RIGHT = 3
        const val MOVE_UP = 4
        const val MOVE_DOWN = 5
        const val MOVE_LEFT = 6
        const val MOVE_RIGHT = 7
    }
}