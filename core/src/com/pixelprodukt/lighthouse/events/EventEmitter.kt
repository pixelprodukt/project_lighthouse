package com.pixelprodukt.lighthouse.events

class EventEmitter<T> {

    private val callbacks = mutableListOf<(T) -> Unit>()

    fun subscribe(callback: (T) -> Unit) {
        callbacks.add(callback)
    }

    fun emit(value: T) {
        callbacks.forEach { it(value) }
    }
}