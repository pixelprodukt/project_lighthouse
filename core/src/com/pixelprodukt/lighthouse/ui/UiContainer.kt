package com.pixelprodukt.lighthouse.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch

class UiContainer : UiView() {

    private val children: MutableList<UiView> = mutableListOf()

    override fun renderImplementation(batch: SpriteBatch) {
        children.forEach { child -> child.render(batch) }
    }

    fun addChild(uiView: UiView) {
        uiView.parent = this
        children.add(uiView)
    }
}