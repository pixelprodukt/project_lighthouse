package com.pixelprodukt.lighthouse.system

import com.badlogic.gdx.Gdx

// Wrapper class for libgdx logger
class Console(val className: String) {

    fun log(msg: String) {
        Gdx.app.log(className, msg)
    }
}