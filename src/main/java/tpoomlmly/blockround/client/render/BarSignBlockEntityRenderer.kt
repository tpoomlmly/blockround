package tpoomlmly.blockround.client.render

import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.math.Matrix4f
import net.minecraft.util.math.Vec3f
import tpoomlmly.blockround.Blockround
import tpoomlmly.blockround.block.BarSignBlock
import tpoomlmly.blockround.entity.BarSignBlockEntity

class BarSignBlockEntityRenderer(ctx: BlockEntityRendererFactory.Context) : BlockEntityRenderer<BarSignBlockEntity> {

    private val textRenderer: TextRenderer = ctx.textRenderer

    private val barSignItemStack = ItemStack(Blockround.BAR_SIGN_BLOCK_ITEM, 1)
    private val minecraftClient: MinecraftClient by lazy { MinecraftClient.getInstance() }

    override fun render(  // TODO render venue image
        signEntity: BarSignBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumerProvider: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {  // TODO maybe animate it swinging like a banner
        val blockState = signEntity.cachedState
        matrices.push()  // push a new identity matrix onto the stack
        // Move the sign into place
        matrices.translate(0.5, 0.5, 0.5)
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-(blockState.get(BarSignBlock.FACING)).asRotation()))

        // Render the model
        matrices.push()
        matrices.translate(0.0, 7 / 48.0, 0.0) // compensate for item frame transformations
        val scaleUpFactor = 4 / 3f
        matrices.scale(scaleUpFactor, scaleUpFactor, scaleUpFactor)
        minecraftClient.itemRenderer.renderItem(
            barSignItemStack,
            ModelTransformation.Mode.FIXED,  // item frame mode
            light,
            overlay,
            matrices,
            vertexConsumerProvider,
            0
        )
        matrices.pop()

        // Draw the text on the sign
        // There's probably a way to simplify this bit
        val textScale = 1 / 96f
        val textHeight = -(13 + 4 / 6.0) / 64  // (Pixels to move down + 1/2 text height) / pixels per block
        matrices.push()
        matrices.translate(
            0.0,
            textHeight,
            7 / 150.0  // To separate the text from the sign's surface and move it to the right place
        )
        matrices.scale(textScale, -textScale, textScale)
        renderText(signEntity.venueName, matrices.peek().model, vertexConsumerProvider, light)
        matrices.pop()

        // Same on the other side
        matrices.translate(
            0.0,
            textHeight,
            -7 / 150.0
        )
        matrices.scale(-textScale, -textScale, -textScale)  // mirror and scale down
        renderText(signEntity.venueName, matrices.peek().model, vertexConsumerProvider, light)
        matrices.pop()  // pop the last matrix off the stack
    }

    override fun rendersOutsideBoundingBox(blockEntity: BarSignBlockEntity?) = true

    /**
     * Renders text on one side of the sign.
     */
    private fun renderText(  // TODO add line wrapping
        text: Text,
        matrixModel: Matrix4f,
        vertexConsumerProvider: VertexConsumerProvider,
        light: Int
    ) = textRenderer.draw(
        text,
        -textRenderer.getWidth(text) / 2f,  // centred
        0f,
        0,  // black
        false,
        matrixModel,
        vertexConsumerProvider,
        false,
        0,
        light
    )
}