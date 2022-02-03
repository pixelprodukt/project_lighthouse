package com.pixelprodukt.lighthouse

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.Json
import com.google.gson.Gson
import com.pixelprodukt.lighthouse.screens.*
import com.pixelprodukt.lighthouse.system.GameManager
import ktx.json.fromJson

class ProjectLighthouse : Game() {

    private lateinit var game: GameManager
    private lateinit var worldMapScreen: WorldMapScreen
    private lateinit var mainMenuScreen: MainMenuScreen
    private lateinit var battleScreen: BattleScreen
    private lateinit var characterScreen: CharacterScreen
    private lateinit var testScreen: SceneTestScreen

    lateinit var json: Json
    lateinit var gson: Gson
    lateinit var file: FileHandle
    lateinit var data: TestOne

    override fun create() {
        game = GameManager()
        worldMapScreen = WorldMapScreen(game)
        mainMenuScreen = MainMenuScreen(game)
        battleScreen = BattleScreen(game)
        characterScreen = CharacterScreen(game)
        testScreen = SceneTestScreen(game)
        //Gdx.input.inputProcessor = game.inputHandler
        setScreen(worldMapScreen)

        json = Json()
        gson = Gson()
        file = Gdx.files.internal("data/test.json")
        data = gson.fromJson(file.readString(), TestOne::class.java)
        // Gdx.app.log("json", "${data.attributeOne}, ${data.attributeTwo}, ${data.attributeThree!!.subAttribute}")
        Gdx.app.log("json", "${data}")
    }

    override fun render() {
        screen.render(Gdx.graphics.deltaTime)

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) setScreen(mainMenuScreen)
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) setScreen(worldMapScreen)
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) setScreen(characterScreen)
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) setScreen(testScreen)
    }

    /*override fun dispose() {
        gameScreen.dispose()
    }*/
}

data class TestOne(var attributeOne: String, var attributeTwo: Int, var attributeThree: TestTwo)
data class TestTwo(var subAttribute: String)
/*
class TestOne {
    var attributeOne: String? = null
    var attributeTwo: Int? = null
    var attributeThree: TestTwo? = null
}
class TestTwo {
    var subAttribute: String? = null
}*/
