package com.pixelprodukt.lighthouse.system

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.pixelprodukt.lighthouse.handler.AssetHandler

class AnimationFactory {

    fun createAnimationController(assetHandler: AssetHandler, assets: String): AnimationController {
        val controller = AnimationController()
        controller.animations[AnimationController.IDLE_UP] = createAnimation(assetHandler.assets.get(assets), 0, 0, 16, 1, 0.2f)
        controller.animations[AnimationController.IDLE_DOWN] = createAnimation(assetHandler.assets.get(assets), 0, 16, 16, 1, 0.2f)
        controller.animations[AnimationController.IDLE_LEFT] = createAnimation(assetHandler.assets.get(assets), 0, 32, 16, 1, 0.2f)
        controller.animations[AnimationController.IDLE_RIGHT] = createAnimation(assetHandler.assets.get(assets), 0, 48, 16, 1, 0.2f)
        controller.animations[AnimationController.MOVE_UP] = createAnimation(assetHandler.assets.get(assets), 0, 0, 16, 2, 0.2f)
        controller.animations[AnimationController.MOVE_DOWN] = createAnimation(assetHandler.assets.get(assets), 0, 16, 16, 2, 0.2f)
        controller.animations[AnimationController.MOVE_LEFT] = createAnimation(assetHandler.assets.get(assets), 0, 32, 16, 2, 0.2f)
        controller.animations[AnimationController.MOVE_RIGHT] = createAnimation(assetHandler.assets.get(assets), 0, 48, 16, 2, 0.2f)
        controller.state = AnimationController.IDLE_DOWN
        return controller
    }

    private fun createAnimation(
        textureSheet: Texture,
        startX: Int,
        startY: Int,
        framesize: Int,
        framecount: Int,
        frameDuration: Float
    ): Animation<TextureRegion> {

        val regionSheet = TextureRegion(textureSheet, startX, startY, framecount * framesize, framesize)
        val regionSheetFrames = arrayOfNulls<TextureRegion>(framecount)

        regionSheetFrames.forEachIndexed { index, it ->
            regionSheetFrames[index] = TextureRegion(regionSheet, index * framesize, 0, framesize, framesize)
        }

        return Animation<TextureRegion>(frameDuration, *regionSheetFrames)
    }
}