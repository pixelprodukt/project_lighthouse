package com.pixelprodukt.lighthouse.map

import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.pixelprodukt.lighthouse.system.Body

class WarpFactory {

    fun createWarpEntry(rectangleMapObject: RectangleMapObject): WarpEntry {
        return WarpEntry(
            rectangleMapObject.properties["id"] as Int,
            Body(
                rectangleMapObject.rectangle.x,
                rectangleMapObject.rectangle.y,
                rectangleMapObject.rectangle.width,
                rectangleMapObject.rectangle.height
            )
        )
    }

    fun createWarpExit(rectangleMapObject: RectangleMapObject): WarpExit {
        return WarpExit(
            rectangleMapObject.properties["targetEntryId"] as Int,
            rectangleMapObject.properties["targetMapName"].toString(),
            Body(
                rectangleMapObject.rectangle.x,
                rectangleMapObject.rectangle.y,
                rectangleMapObject.rectangle.width,
                rectangleMapObject.rectangle.height
            )
        )
    }
}