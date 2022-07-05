package com.pixelprodukt.lighthouse.interfaces

import com.pixelprodukt.lighthouse.gameobjects.UpdateState

interface Updatable {
    fun update(state: UpdateState)
}