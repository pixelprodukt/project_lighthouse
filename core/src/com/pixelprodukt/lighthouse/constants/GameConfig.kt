package com.pixelprodukt.lighthouse.constants

object GameConfig {

    const val WINDOW_WIDTH =  1024 //768
    const val WINDOW_HEIGHT = 768 //640

    const val SCALE_FACTOR = 4

    const val VIEWPORT_WIDTH = (WINDOW_WIDTH / SCALE_FACTOR).toFloat() //256f //192f
    const val VIEWPORT_HEIGHT = (WINDOW_HEIGHT / SCALE_FACTOR).toFloat() //192f //160f

    const val RESIZABLE = false
    /**
     * Viewport is the area where the camera is rendered.
     * If the camera width/height is smaller than the viewport, it will get strechted.
     * Keep the aspect ratio to get a nice pixelated style.
     */
    const val worldViewportWidth = VIEWPORT_WIDTH * SCALE_FACTOR
    const val worldViewportHeight = VIEWPORT_HEIGHT * SCALE_FACTOR
}
