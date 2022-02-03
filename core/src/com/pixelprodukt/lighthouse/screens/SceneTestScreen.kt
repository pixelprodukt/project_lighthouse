package com.pixelprodukt.lighthouse.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.scenes.scene2d.*
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ScalingViewport
import com.pixelprodukt.lighthouse.constants.Assets
import com.pixelprodukt.lighthouse.constants.GameConfig
import com.pixelprodukt.lighthouse.system.Console
import com.pixelprodukt.lighthouse.system.GameManager
import com.pixelprodukt.lighthouse.ui.`scene2d-widget`.TextButtonWithMarker
import com.ray3k.stripe.FreeTypeSkin
import ktx.app.KtxScreen


class SceneTestScreen(private val game: GameManager) : KtxScreen {

    private val console = Console(SceneTestScreen::class.simpleName!!)

    private val camera = OrthographicCamera(GameConfig.VIEWPORT_WIDTH, GameConfig.VIEWPORT_HEIGHT)

    private val texture = Texture(Assets.TEXTBOX_PATCH)
    private val ninePatch = NinePatch(texture, 4, 4, 4, 4)
    private val ninePatchDrawable = NinePatchDrawable(ninePatch)

    private val stage = Stage(ScalingViewport(Scaling.fill, GameConfig.VIEWPORT_WIDTH, GameConfig.VIEWPORT_HEIGHT ,camera))

    private val buttonGrp = createTableWithPresets(4f, 34f, 76f, 154f, true)

    private var fontskin = FreeTypeSkin(Gdx.files.internal("skin/lighthouse.json"))

    private var inventoryBtn: TextButtonWithMarker = TextButtonWithMarker("Invent.", fontskin)
    private var statsBtn: TextButtonWithMarker = TextButtonWithMarker("Stats", fontskin)

    private val inventoryContextMenu = createTableWithPresets(84f, 34f, 168f, 154f, false)
    private var statContextMenu = createTableWithPresets(84f, 34f, 168f, 154f, false)
    private var attributesContextMenu = createTableWithPresets(84f, 34f, 168f, 154f, false)

    init {
        // Gdx.input.inputProcessor = stage

        buttonGrp.add(inventoryBtn).align(Align.left)
        buttonGrp.row()
        buttonGrp.add(statsBtn).align(Align.left)

        //inventoryBtn.debug = true
        //statsBtn.debug = true

        inventoryBtn.addListener(object: InputListener() {
            override fun keyUp(event: InputEvent?, keycode: Int): Boolean {
                if (keycode == Input.Keys.E) {
                    inventoryContextMenu.isVisible = true
                    statContextMenu.isVisible = false
                    stage.keyboardFocus = inventoryContextMenu.getChild(0)
                }
                return true
            }
        })

        statsBtn.addListener(object: InputListener() {
            override fun keyUp(event: InputEvent?, keycode: Int): Boolean {
                if (keycode == Input.Keys.E) {
                    inventoryContextMenu.isVisible = false
                    statContextMenu.isVisible = true
                    // stage.keyboardFocus = statContextMenu.getChild(0)
                }
                return true
            }
        })

        inventoryContextMenu.debug = true

        inventoryContextMenu.add(TextButtonWithMarker("Cola", fontskin)).expandX().align(Align.left)
        inventoryContextMenu.add(Label("2", fontskin)).align(Align.right)
        inventoryContextMenu.row()
        inventoryContextMenu.add(TextButtonWithMarker("m. Stimpack", fontskin)).expandX().align(Align.left)
        inventoryContextMenu.add(Label("11", fontskin)).align(Align.right)
        inventoryContextMenu.row()
        inventoryContextMenu.add(TextButtonWithMarker("Trash", fontskin)).expandX().align(Align.left)
        inventoryContextMenu.add(Label("8", fontskin)).align(Align.right)
        inventoryContextMenu.row()
        inventoryContextMenu.add(TextButtonWithMarker("Money", fontskin)).expandX().align(Align.left)
        inventoryContextMenu.add(Label("273", fontskin)).align(Align.right)
        inventoryContextMenu.row()
        inventoryContextMenu.add(TextButtonWithMarker("Grenade", fontskin)).expandX().align(Align.left)
        inventoryContextMenu.add(Label("5", fontskin)).align(Align.right)
        inventoryContextMenu.row()

        stage.addActor(buttonGrp)
        stage.addActor(inventoryContextMenu)
        stage.addActor(statContextMenu)
        stage.keyboardFocus = buttonGrp.getChild(0)
    }

    private fun addInventoryButtons(inventoryContextMenu: Table) {
        val buttons = listOf(
            TextButtonWithMarker("02 Cola", fontskin),
            TextButtonWithMarker("01 Weapon", fontskin),
            TextButtonWithMarker("09 s.Stim.", fontskin),
            TextButtonWithMarker("02 m.Stim.", fontskin),
            TextButtonWithMarker("12 Grenade", fontskin),
            TextButtonWithMarker("03 Trash", fontskin),
            TextButtonWithMarker("92 Money", fontskin),
            TextButtonWithMarker("01 Liver", fontskin),
        )

        val cells = mutableListOf<Cell<TextButtonWithMarker>>()

        cells.add(inventoryContextMenu.add().align(Align.left) as Cell<TextButtonWithMarker>)
        cells.add(inventoryContextMenu.add().align(Align.left) as Cell<TextButtonWithMarker>)
        inventoryContextMenu.row()
        cells.add(inventoryContextMenu.add().align(Align.left) as Cell<TextButtonWithMarker>)
        cells.add(inventoryContextMenu.add().align(Align.left) as Cell<TextButtonWithMarker>)
        inventoryContextMenu.row()
        cells.add(inventoryContextMenu.add().align(Align.left) as Cell<TextButtonWithMarker>)
        cells.add(inventoryContextMenu.add().align(Align.left) as Cell<TextButtonWithMarker>)
        inventoryContextMenu.row()
        cells.add(inventoryContextMenu.add().align(Align.left) as Cell<TextButtonWithMarker>)
        cells.add(inventoryContextMenu.add().align(Align.left) as Cell<TextButtonWithMarker>)
        inventoryContextMenu.row()

        // TODO: extract listener and use logic for items
        buttons.forEachIndexed() { index, btn ->
            btn.addListener(object: InputListener() {
                override fun keyUp(event: InputEvent?, keycode: Int): Boolean {
                    if (keycode == Input.Keys.E) {
                        val itemContextMenu = createTableWithPresets(92f, 72f, 68f, 42f, true)
                        val equipBtn = TextButtonWithMarker("Equip", fontskin)

                        itemContextMenu.add(equipBtn).align(Align.left)
                        stage.addActor(itemContextMenu)

                        equipBtn.addListener(object: InputListener() {
                            override fun keyUp(event: InputEvent?, keycode: Int): Boolean {
                                if (keycode == Input.Keys.Q) {
                                    equipBtn.remove()
                                    itemContextMenu.remove()
                                    stage.keyboardFocus = btn
                                }
                                return true
                            }
                        })
                        stage.keyboardFocus = equipBtn
                    }
                    if (keycode == Input.Keys.Q) {
                        inventoryContextMenu.isVisible = false
                        stage.keyboardFocus = buttonGrp.getChild(0)
                    }
                    return true
                }
            })
            if (index == 0) cells[0].setActor(btn)
            if (index == 1) cells[2].setActor(btn)
            if (index == 2) cells[4].setActor(btn)
            if (index == 3) cells[6].setActor(btn)
            if (index == 4) cells[1].setActor(btn)
            if (index == 5) cells[3].setActor(btn)
            if (index == 6) cells[5].setActor(btn)
            if (index == 7) cells[7].setActor(btn)

        }
    }

    private fun nextButton() {
        val parent = stage.keyboardFocus.parent
        val children = parent.children

        if ((children.indexOf(stage.keyboardFocus) + 1) == children.size) {
            stage.keyboardFocus = parent.getChild(0)
        } else {
            stage.keyboardFocus = parent.getChild(children.indexOf(stage.keyboardFocus) + 1)
            if (stage.keyboardFocus is Label) nextButton()
        }
    }

    private fun previousButton() {
        val parent = stage.keyboardFocus.parent
        val children = parent.children

        if ((children.indexOf(stage.keyboardFocus) - 1) < 0) {
            stage.keyboardFocus = parent.getChild(children.size - 1)
            if (stage.keyboardFocus is Label) previousButton()
        } else {
            stage.keyboardFocus = parent.getChild(children.indexOf(stage.keyboardFocus) - 1)
            if (stage.keyboardFocus is Label) previousButton()
        }
    }

    private fun createTableWithPresets(x: Float, y: Float, width: Float, height: Float, isVisible: Boolean): Table {
        val table = Table()
        table.background = ninePatchDrawable
        table.width = width
        table.height = height
        table.setPosition(x, y)
        table.pad(6f).align(Align.topLeft)
        table.defaults().pad(0f, 4f, 0f, 4f)
        //table.debug = true
        table.isVisible = isVisible
        return table
    }

    override fun render(delta: Float) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) previousButton()
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) nextButton()

        camera.position.set(GameConfig.VIEWPORT_WIDTH / 2, GameConfig.VIEWPORT_HEIGHT / 2, 0f)
        camera.update()
        stage.act(Gdx.graphics.deltaTime)
        stage.draw()
    }

    override fun show() {
        super.show()
        Gdx.input.inputProcessor = stage
    }
}