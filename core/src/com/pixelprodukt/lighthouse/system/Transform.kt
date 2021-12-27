package com.pixelprodukt.lighthouse.system

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

data class Transform(
    val position: Vector3 = Vector3(0.0f, 0.0f, 0.0f),
    val offset: Vector3 = Vector3(0.0f, 0.0f, 0.0f),
    val originOffset: Vector3 = Vector3(0.0f, 0.0f, 0.0f),
    val scale: Vector2 = Vector2(1.0f, 1.0f),
    var rotation: Float = 0.0f
)