package tpoomlmly.blockround.client.ui

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.ScreenTexts
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.render.DiffuseLighting
import net.minecraft.client.util.SelectionManager
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import tpoomlmly.blockround.Blockround
import tpoomlmly.blockround.entity.BarSignBlockEntity
import tpoomlmly.blockround.network.barSignUpdateC2SBuffer

class BarSignEditor constructor(private val sign: BarSignBlockEntity) : Screen(TranslatableText("barsign.edit")) {

    private lateinit var selectionManager: SelectionManager
    private var ticksSinceOpened = 0
    private var venueName: String = sign.venueName.string
    private val venueId = 0

    override fun init() {
        this.client!!.keyboard.setRepeatEvents(true)
        this.addDrawableChild(
            ButtonWidget(
                this.width / 2 - 100,
                this.height / 4 + 120,
                200,
                20,
                ScreenTexts.DONE
            ) { this.finishEditing() }
        )
        this.sign.editable = false
        this.selectionManager = SelectionManager(
            { venueName },
            { venueName = it; sign.updateSign(Text.of(it), venueId) },
            SelectionManager.makeClipboardGetter(this.client),
            SelectionManager.makeClipboardSetter(this.client),
            { this.client!!.textRenderer.getWidth(it) <= 90 }
        )
    }

    override fun removed() {
        this.client!!.keyboard.setRepeatEvents(false)
        ClientPlayNetworking.send(Blockround.BAR_SIGN_CHANNEL, barSignUpdateC2SBuffer(sign.pos, venueName, venueId))
        sign.editable = true
    }

    /**
     * I think this closes the screen if someone breaks the sign
     */
    override fun tick() {
        ticksSinceOpened++
        if (!sign.type.supports(sign.cachedState)) finishEditing()
    }

    private fun finishEditing() {
        sign.markDirty()
        client!!.openScreen(null)  // calls this.removed()
    }

    override fun charTyped(chr: Char, modifiers: Int): Boolean {
        selectionManager.insert(chr)
        return true
    }

    override fun onClose() = finishEditing()

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int) =
        if (selectionManager.handleSpecialKey(keyCode)) true else super.keyPressed(keyCode, scanCode, modifiers)

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        DiffuseLighting.disableGuiDepthLighting()
        this.renderBackground(matrices)
        DrawableHelper.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 40, 16777215)

        // move the sign into place
        matrices.push()
        matrices.translate(this.width / 2.0, 0.0, 50.0)
        val scaleFactor = 93.75f
        matrices.scale(scaleFactor, scaleFactor, scaleFactor)
        matrices.translate(0.0, -1.3125, 0.0)

        matrices.pop()
        DiffuseLighting.enableGuiDepthLighting()
        super.render(matrices, mouseX, mouseY, delta)
    }
}