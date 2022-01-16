package com.pixelprodukt.lighthouse.ui

class SimpleTextBox(width: Float = 20f, height: Float = 20f) : UiContainer(0f, 0f, 256f, 192f) {

    private val uiNinePatch: UiNinePatch
    private val uiText: UiText
    private val uiTextBoxMarker: UiTextBoxMarker

    private val listeners: MutableList<() -> Unit> = mutableListOf()
    private var messages = mutableListOf<String>()
    private var index = 0

    init {
        uiNinePatch = UiNinePatch(width, height)
        uiText = UiText()
        uiTextBoxMarker = UiTextBoxMarker()
        uiNinePatch.centerOn(this)
        uiNinePatch.alignToBottomOf(this, 4f)
        uiText.alignToTopLeftOf(uiNinePatch, 6f)
        uiText.boundariesX = uiNinePatch.width - 12f
        uiTextBoxMarker.alignToBottomRightOf(uiNinePatch, 5f)
        addChild(uiNinePatch)
        addChild(uiText)
        addChild(uiTextBoxMarker)
        init(mutableListOf(""))
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