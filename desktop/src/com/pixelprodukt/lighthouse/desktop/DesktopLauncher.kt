package com.pixelprodukt.lighthouse.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.pixelprodukt.lighthouse.ProjectLighthouse

fun main() {

    val config = LwjglApplicationConfiguration().apply {
        width = 1024
        height = 768
        resizable = false
    }

    LwjglApplication(ProjectLighthouse(), config)
}