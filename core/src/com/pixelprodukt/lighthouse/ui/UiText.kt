package com.pixelprodukt.lighthouse.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import com.badlogic.gdx.utils.Align
import com.pixelprodukt.lighthouse.constants.Assets

class UiText(var text: String = "", var fontSize: Int = 8, var color: Color = Color.WHITE, var alignment: Int = Align.topLeft) : UiView() {

    val generator = FreeTypeFontGenerator(Gdx.files.internal(Assets.FONT))
    val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
    var font: BitmapFont
    var boundariesX: Float = 0f

    override var width: Float = 0f
        get() {
            val layout = GlyphLayout(font, text)
            return layout.width
        }

    override var height: Float = 0f
        get() {
            val layout = GlyphLayout(font, text)
            return layout.height
        }

    init {
        parameter.size = fontSize
        parameter.spaceY = -1
        //parameter.spaceX = -1
        font = generator.generateFont(parameter)
        font.color = color
        font.color = Color(48f / 255f, 104f / 255f, 80f / 255f, 1f)
    }

    override fun renderImplementation(batch: SpriteBatch) {
        font.draw(batch, text, x, y + height, boundariesX, alignment, true)
    }
}