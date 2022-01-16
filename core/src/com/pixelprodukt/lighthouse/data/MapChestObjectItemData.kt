package com.pixelprodukt.lighthouse.data

import kotlinx.serialization.Serializable

@Serializable
data class MapChestObjectItemData(
    var label: String,
    var type: String,
    var quantity: Int
)
