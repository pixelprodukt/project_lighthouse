package com.pixelprodukt.lighthouse.interfaces

import com.pixelprodukt.lighthouse.system.Body

interface Interactable {
    val sensor: Body
    val listeners: MutableList<(Event: Any) -> Unit>
    fun interact()
    fun addInteractionListener(callback: (Event: Any) -> Unit) { listeners.add(callback) }
    fun removeAllListeners() { listeners.clear() }
}