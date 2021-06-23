package tpoomlmly.blockround.client.ui

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.ScreenTexts
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.render.*
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer
import net.minecraft.client.util.SelectionManager
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.SignType
import tpoomlmly.blockround.Blockround
import tpoomlmly.blockround.entity.BarSignBlockEntity
import tpoomlmly.blockround.network.barSignUpdateC2SBuffer
import java.util.*
import kotlin.math.max
import kotlin.math.min

class BarSignEditor constructor(private val sign: BarSignBlockEntity) : Screen(TranslatableText("barsign.edit")) {

    private lateinit var selectionManager: SelectionManager
    private var ticksSinceOpened = 0
    private lateinit var model: SignBlockEntityRenderer.SignModel
    private val signType = SignType.DARK_OAK

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
        sign.editable = false
        selectionManager = SelectionManager(
            { venueName },
            { venueName = it; sign.updateSign(Text.of(it), venueId) },
            SelectionManager.makeClipboardGetter(this.client),
            SelectionManager.makeClipboardSetter(this.client),
            { this.client!!.textRenderer.getWidth(it) <= 90 }
        )
        model = SignBlockEntityRenderer.createSignModel(this.client!!.entityModelLoader, signType)
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
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 40, 0xffffff)
        matrices.push()

        // move the sign into place
        matrices.translate(this.width / 2.0, 0.0, 50.0)
        val scaleFactor = 93.75f
        matrices.scale(scaleFactor, scaleFactor, scaleFactor)
        matrices.translate(0.0, -1.3125, 0.0)

        matrices.push()
        matrices.scale(2 / 3f, 2 / 3f, 2 / 3f)
        val vertexConsumerProvider = this.client!!.bufferBuilders.entityVertexConsumers
        val spriteIdentifier = TexturedRenderLayers.getSignTextureId(signType)
        val vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumerProvider, model::getLayer)
        model.stick.visible = false
        model.root.render(matrices, vertexConsumer, 0xf000f0, OverlayTexture.DEFAULT_UV)
        matrices.pop()

        matrices.translate(0.0, 1 / 3.0, 7 / 150.0)
        matrices.scale(1 / 96f, -1 / 96f, 1 / 96f)

        val signColour = 0x000000
        val selectionStart = selectionManager.selectionStart
        val selectionEnd = selectionManager.selectionEnd
        val yOffset = -5
        val modelMatrix = matrices.peek().model

        client!!.textRenderer.draw(
            venueName,
            -client!!.textRenderer.getWidth(venueName) / 2f,
            -5f,
            signColour,
            false,
            modelMatrix,
            vertexConsumerProvider,
            false,
            0,
            0xf000f0,
            false
        )

        vertexConsumerProvider.draw()

        if (selectionStart >= 0) {
            val horizontalOffset =
                client!!.textRenderer.getWidth(
                    venueName.substring(
                        0,
                        max(min(selectionStart, venueName.length), 0)
                    )
                ) - client!!.textRenderer.getWidth(venueName) / 2
            if (ticksSinceOpened / 6 % 2 == 0 && selectionStart < venueName.length) {
                fill(matrices, horizontalOffset, yOffset, horizontalOffset + 1, yOffset + 9, -16777216 or signColour)
            }

            if (selectionStart != selectionEnd) {
                val t = selectionStart.coerceAtMost(selectionEnd)
                val u = selectionStart.coerceAtLeast(selectionEnd)
                val v = client!!.textRenderer.getWidth(
                    venueName.substring(
                        0,
                        t
                    )
                ) - client!!.textRenderer.getWidth(venueName) / 2
                val w: Int = client!!.textRenderer.getWidth(
                    venueName.substring(
                        0,
                        u
                    )
                ) - client!!.textRenderer.getWidth(venueName) / 2
                val x = v.coerceAtMost(w)
                val y = v.coerceAtLeast(w)
                val tessellator = Tessellator.getInstance()
                val bufferBuilder = tessellator.buffer
                RenderSystem.setShader { GameRenderer.getPositionColorShader() }
                RenderSystem.disableTexture()
                RenderSystem.enableColorLogicOp()
                RenderSystem.logicOp(GlStateManager.LogicOp.OR_REVERSE)
                bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR)
                var var32 = x.toFloat()
                Objects.requireNonNull(client!!.textRenderer)
                bufferBuilder.vertex(modelMatrix, var32, (yOffset + 9).toFloat(), 0.0f).color(0, 0, 255, 255).next()
                var32 = y.toFloat()
                Objects.requireNonNull(client!!.textRenderer)
                bufferBuilder.vertex(modelMatrix, var32, (yOffset + 9).toFloat(), 0.0f).color(0, 0, 255, 255).next()
                bufferBuilder.vertex(modelMatrix, y.toFloat(), yOffset.toFloat(), 0.0f).color(0, 0, 255, 255).next()
                bufferBuilder.vertex(modelMatrix, x.toFloat(), yOffset.toFloat(), 0.0f).color(0, 0, 255, 255).next()
                bufferBuilder.end()
                BufferRenderer.draw(bufferBuilder)
                RenderSystem.disableColorLogicOp()
                RenderSystem.enableTexture()
            }
        }

//        matrices.push()
//        matrices.translate(0.0, 7 / 48.0, 0.0) // compensate for item frame transformations
//        val scaleUpFactor = 4 / 3f
//        matrices.scale(scaleUpFactor, scaleUpFactor, scaleUpFactor)
//        val vertexConsumerProvider: VertexConsumerProvider.Immediate = client!!.bufferBuilders.entityVertexConsumers
//        client!!.itemRenderer.renderItem(
//            barSignItemStack,
//            ModelTransformation.Mode.FIXED,  // item frame mode
//            0xf0_00_f0,
//            OverlayTexture.DEFAULT_UV,
//            matrices,
//            vertexConsumerProvider,
//            0
//        )
//        matrices.pop()

        matrices.pop()
        DiffuseLighting.enableGuiDepthLighting()
        super.render(matrices, mouseX, mouseY, delta)
    }
}