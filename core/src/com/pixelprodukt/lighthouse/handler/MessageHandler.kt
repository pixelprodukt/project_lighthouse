package com.pixelprodukt.lighthouse.handler

object MessageHandler {

    private val listeners = mutableListOf<(messages: MutableList<String>) -> Unit>()

    fun addMessageListener(callback: (messages: MutableList<String>) -> Unit) {
        listeners.add(callback)
    }

    fun removeAllListeners() {
        listeners.clear()
    }

    fun publishMessages(messages: MutableList<String>) {
        listeners.forEach { listener -> listener(messages) }
    }
}