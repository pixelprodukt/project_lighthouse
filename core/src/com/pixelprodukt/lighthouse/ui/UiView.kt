package com.pixelprodukt.lighthouse.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pixelprodukt.lighthouse.interfaces.Renderable
import com.pixelprodukt.lighthouse.interfaces.Updatable
import kotlin.math.withSign

open abstract class UiView(
    var x: Float = 0f,
    var y: Float = 0f,
    open var width: Float = 0f,
    open var height: Float = 0f
) : Renderable {

    var isVisible = true
    var isActive = true
    var parent: UiContainer? = null

    var scaleX: Float = 0f
    var scaleY: Float = 0f
    var speed: Float = 0f

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

    fun alignBottomOf(other: UiView, padding: Float = 0f) {
        y = other.y + padding
    }

    fun alignBottomLeftOf(other: UiView, padding: Float = 0f) {
        x = other.x + padding
        y = other.y + padding
    }

    fun alignBottomRightOf(other: UiView, padding: Float = 0f) {
        x = (other.x + other.width) - width - padding
        y = other.y + padding
    }

    fun alignTopLeftOf(other: UiView, padding: Float = 0f) {
        x = other.x + padding
        y = other.y + other.height - height - padding
    }

    fun centerOn(other: UiView) {
        x = ((other.x + other.width) / 2) - (width / 2)
        y = ((other.y + other.height) / 2) - (height / 2)
    }

    fun centerOnX(other: UiView) {
        x = (other.width / 2) - (width / 2)
    }
}