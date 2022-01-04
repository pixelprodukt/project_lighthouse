package com.pixelprodukt.lighthouse.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pixelprodukt.lighthouse.interfaces.Renderable

open abstract class UiView() : Renderable {

    var isVisible = true
    var parent: UiContainer? = null
    var x: Float = 0f
    var y: Float = 0f
    var scaleX: Float = 0f
    var scaleY: Float = 0f
    var speed: Float = 0f

    open var width: Float = 0f
    open var height: Float = 0f

    fun xy(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    fun addTo(parent: UiContainer) {
        this.parent = parent
    }

    final override fun render(batch: SpriteBatch) {
        if (!isVisible) return
        renderImplementation(batch)
    }

    protected abstract fun renderImplementation(batch: SpriteBatch)
}