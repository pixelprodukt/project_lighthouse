package com.pixelprodukt.lighthouse.handler

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.pixelprodukt.lighthouse.system.Direction

class InputHandler : InputProcessor {

    var isUpPressed: Boolean = false
    var isDownPressed: Boolean = false
    var isLeftPressed: Boolean = false
    var isRightPressed: Boolean = false

    var leftMousePressed: Boolean = false
    var rightMousePressed: Boolean = false

    var isSpacePressed: Boolean = false
    var isBackPressed: Boolean = false
    var isActionPressed: Boolean = false
    var isInventoryPressed: Boolean = false
    var isDebug: Boolean = false

    private val pressedKeysList = mutableListOf<Direction>()
    val pressedKey: Direction? get() {
        if (pressedKeysList.isNotEmpty()) {
            return pressedKeysList[0]
        }
        return null
    }

    private val keymap = hashMapOf(
        Input.Keys.UP to Direction.UP,
        Input.Keys.LEFT to Direction.LEFT,
        Input.Keys.DOWN to Direction.DOWN,
        Input.Keys.RIGHT to Direction.RIGHT,
        Input.Keys.W to Direction.UP,
        Input.Keys.A to Direction.LEFT,
        Input.Keys.S to Direction.DOWN,
        Input.Keys.D to Direction.RIGHT
    )

    override fun keyDown(keycode: Int): Boolean {

        when (keycode) {
            Input.Keys.W, Input.Keys.UP -> isUpPressed = true
            Input.Keys.A, Input.Keys.LEFT -> isLeftPressed = true
            Input.Keys.S, Input.Keys.DOWN -> isDownPressed = true
            Input.Keys.D, Input.Keys.RIGHT -> isRightPressed = true
            Input.Keys.Q -> isBackPressed = true
            Input.Keys.SPACE -> isSpacePressed = true
            Input.Keys.E -> isActionPressed = true
            Input.Keys.TAB -> isInventoryPressed = true
            Input.Keys.BACKSPACE -> isDebug = !isDebug
        }

        val key = keymap.get(keycode)

        if (key != null && pressedKeysList.indexOf(key) == -1) {
            pressedKeysList.add(0, key)
        }
        return false
    }

    override fun keyUp(keycode: Int): Boolean {

        when (keycode) {
            Input.Keys.W, Input.Keys.UP -> isUpPressed = false
            Input.Keys.A, Input.Keys.LEFT -> isLeftPressed = false
            Input.Keys.S, Input.Keys.DOWN -> isDownPressed = false
            Input.Keys.D, Input.Keys.RIGHT -> isRightPressed = false
            Input.Keys.Q -> isBackPressed = false
            Input.Keys.SPACE -> isSpacePressed = false
            Input.Keys.E -> isActionPressed = false
            Input.Keys.TAB -> isInventoryPressed = false
        }

        val key = keymap.get(keycode)
        val index = pressedKeysList.indexOf(key)

        if (index > -1) {
            pressedKeysList.removeAt(index)
        }
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {

        if (button == Input.Buttons.LEFT) leftMousePressed = true
        if (button == Input.Buttons.RIGHT) rightMousePressed = true

        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {

        if (button == Input.Buttons.LEFT) leftMousePressed = false
        if (button == Input.Buttons.RIGHT) rightMousePressed = false

        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return false
    }
}