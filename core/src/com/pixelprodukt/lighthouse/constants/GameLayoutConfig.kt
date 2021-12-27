package com.pixelprodukt.lighthouse.constants

object GameLayoutConfig {

    /**
     * All viewports have to add up to the total size of the window
     */
    const val windowWidth =  1024 //768
    const val windowHeight = 768 //640

    /**
     * zoom factor for the pixelated style, e.g. how 'big' the pixels will get
     */
    const val zoomFactor = 3

    /**
     * The camera is the effective size of things that get rendered in it.
     * As we want to achieve a pixelated style, we have to set the viewport like:
     * camerasize * zoom factor
     */
    const val worldCameraWidth = 256f //192f
    const val worldCameraHeight = 192f //160f

    const val dialogCameraWidth = 0f
    const val dialogCameraHeight = 0f

    /**
     * Viewport is the area where the camera is rendered.
     * If the camera width/height is smaller than the viewport, it will get strechted.
     * Keep the aspect ratio to get a nice pixelated style.
     */
    const val worldViewportWidth = (worldCameraWidth * zoomFactor).toInt()
    const val worldViewportHeight = (worldCameraHeight * zoomFactor).toInt()

    const val dialogViewportWidth = (dialogCameraWidth * zoomFactor).toInt()
    const val dialogViewportHeight = (dialogCameraHeight * zoomFactor).toInt()

    /**
     * Position for the viewport
     */
    const val worldViewportPositionX = 0
    const val worldViewportPositionY = 0


}
