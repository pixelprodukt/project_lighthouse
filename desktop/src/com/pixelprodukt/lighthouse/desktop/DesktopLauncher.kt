package com.pixelprodukt.lighthouse.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.pixelprodukt.lighthouse.ProjectLighthouse
import com.pixelprodukt.lighthouse.constants.GameConfig

fun main() {

    val config = LwjglApplicationConfiguration().apply {
        width = GameConfig.WINDOW_WIDTH
        height = GameConfig.WINDOW_HEIGHT
        resizable = GameConfig.RESIZABLE
    }

    LwjglApplication(ProjectLighthouse(), config)
}