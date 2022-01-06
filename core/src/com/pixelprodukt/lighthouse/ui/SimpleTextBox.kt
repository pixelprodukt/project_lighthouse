package com.pixelprodukt.lighthouse.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pixelprodukt.lighthouse.constants.Assets
import com.pixelprodukt.lighthouse.handler.AssetHandler

class SimpleTextBox() : UiContainer(0f, 0f, 256f, 192f) {

    private val uiNinePatch: UiNinePatch
    private val uiText: UiText
    private val uiTextBoxMarker: UiTextBoxMarker

    private val listeners: MutableList<() -> Unit> = mutableListOf()
    private var messages = mutableListOf<String>()
    private var index = 0

    init {
        uiNinePatch = UiNinePatch(248f, 45f)
        uiText = UiText("This is a test. Here follows some longer text to test out the readablity and how the thing...")
        uiTextBoxMarker = UiTextBoxMarker()
        uiNinePatch.centerOn(this)
        uiNinePatch.alignBottomOf(this, 4f)
        uiText.alignTopLeftOf(uiNinePatch, 6f)
        uiText.boundariesX = uiNinePatch.width - 12f
        uiTextBoxMarker.alignBottomRightOf(uiNinePatch, 5f)
        addChild(uiNinePatch)
        addChild(uiText)
        addChild(uiTextBoxMarker)
    }

    fun init(messages: MutableList<String>) {
        this.messages = messages
        index = 0
        if (messages.size >= 1) {
            uiText.text = messages[index]
        }
        uiTextBoxMarker.isVisible = messages.size > 1
    }

    fun nextMessage() {
        if (index < messages.size - 1) {
            index++
            uiText.text = messages[index]
        } else {
            index = 0
            uiText.text = messages[index]
            listeners.forEach { listener -> listener() }
        }
        if (index == messages.size - 1) {
            uiTextBoxMarker.isVisible = false
        }
    }

    fun onClose(listener: () -> Unit) {
        listeners.add(listener)
    }
}