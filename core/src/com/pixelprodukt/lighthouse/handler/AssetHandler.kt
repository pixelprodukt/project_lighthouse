package com.pixelprodukt.lighthouse.handler

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import com.pixelprodukt.lighthouse.constants.Assets

class AssetHandler {

    val assets = AssetManager().apply {

        load(Assets.PLAYER, Texture::class.java)
        load(Assets.GIRL_01, Texture::class.java)
        load(Assets.TEXTBOX_PATCH, Texture::class.java)
        load(Assets.MENU_MARKER, Texture::class.java)
        load(Assets.TEXTBOX_ARROW_FULL, Texture::class.java)
        load(Assets.TEXTBOX_ARROW_EMPTY, Texture::class.java)
        load(Assets.TEXTBOX_MARKER_SELECTED, Texture::class.java)
        load(Assets.TEXTBOX_MARKER_UNSELECTED, Texture::class.java)
        load(Assets.DIALOG_ICON, Texture::class.java)
        load(Assets.INTERACTION_ICON, Texture::class.java)
        load(Assets.MENU_SELECTION_ARROW, Texture::class.java)
        load(Assets.SIGN, Texture::class.java)

        val resolver = InternalFileHandleResolver()
        setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(resolver))
        setLoader(BitmapFont::class.java,".ttf", FreetypeFontLoader(resolver))

        val fontLoaderParameter = FreetypeFontLoader.FreeTypeFontLoaderParameter()
        fontLoaderParameter.fontFileName = Assets.FONT
        fontLoaderParameter.fontParameters.size = 8
        fontLoaderParameter.fontParameters.spaceY = -1
        //fontLoaderParameter.fontParameters.color = Color(48f / 255f, 104f / 255f, 80f / 255f, 1f)

        load(Assets.FONT, BitmapFont::class.java, fontLoaderParameter)

        finishLoading()
    }

    fun getFont() : BitmapFont { return assets.get(Assets.FONT) }
}