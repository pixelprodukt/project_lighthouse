package com.pixelprodukt.lighthouse.interfaces

import com.pixelprodukt.lighthouse.UpdateState

interface Updatable {
    fun update(delta: Float, state: UpdateState)
}