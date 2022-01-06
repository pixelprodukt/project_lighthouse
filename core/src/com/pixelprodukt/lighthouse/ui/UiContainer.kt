package com.pixelprodukt.lighthouse.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch

open class UiContainer(
    x: Float = 0f,
    y: Float = 0f,
    override var width: Float = 0f,
    override var height: Float = 0f
) : UiView(x, y) {

    private val children: MutableList<UiView> = mutableListOf()

    override fun renderImplementation(batch: SpriteBatch) {
        children.forEach { child -> child.render(batch) }
    }

    fun addChild(uiView: UiView) {
        uiView.parent = this
        children.add(uiView)
    }
}