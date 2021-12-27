package com.pixelprodukt.lighthouse.ui

class MenuSelection(
    val label: String,
    val data: Any? = null,
    val subMenu: List<Any>? = null,
    var isSelected: Boolean = false,
    var isDisabled: Boolean = false
)