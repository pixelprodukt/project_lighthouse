package com.pixelprodukt.lighthouse.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pixelprodukt.lighthouse.interfaces.Renderable

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

    fun alignToBottomOf(other: UiView, padding: Float = 0f) {
        y = other.y + padding
    }

    fun alignToBottomLeftOf(other: UiView, padding: Float = 0f) {
        x = other.x + padding
        y = other.y + padding
    }

    fun alignToBottomRightOf(other: UiView, padding: Float = 0f) {
        x = (other.x + other.width) - width - padding
        y = other.y + padding
    }

    fun alignToTopOf(other: UiView, padding: Float = 0f) {
        y = other.y + other.height - height - padding
    }

    fun alignToTopLeftOf(other: UiView, padding: Float = 0f) {
        x = other.x + padding
        y = other.y + other.height - height - padding
    }

    fun alignToLeftSideOf(other: UiView, padding: Float = 0f) {
        x = other.x - width - padding
    }

    fun alignToRightSideOf(other: UiView, padding: Float = 0f) {
        x = other.x + other.width + padding
    }

    fun alignToTopRightSideOf(other: UiView, padding: Float = 0f) {
        x = other.x + other.width + padding
        y = other.y + other.height - height
    }

    fun alignToBottomSideOf(other: UiView, padding: Float = 0f) {
        x = other.x
        y = other.y - height - padding
    }

    fun centerOn(other: UiView) {
        x = ((other.x + other.width) / 2) - (width / 2)
        y = ((other.y + other.height) / 2) - (height / 2)
    }

    fun centerOnXOf(other: UiView) {
        x = (other.x + other.width / 2) - (width / 2)
    }

    fun centerOnYOf(other: UiView) {
        y = (other.y + other.height / 2) - (height / 2)
    }
}