package tpoomlmly.blockround.client.render

import net.minecraft.client.font.TextRenderer
import net.minecraft.client.model.Model
import net.minecraft.client.model.ModelPart
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Vec3f
import tpoomlmly.blockround.block.BarSignBlock
import tpoomlmly.blockround.entity.BarSignBlockEntity

class BarSignBlockEntityRenderer(ctx: BlockEntityRendererFactory.Context) : BlockEntityRenderer<BarSignBlockEntity> {

    private val textRenderer: TextRenderer = ctx.textRenderer
//    private val model: BarSignModel =
//        BarSignModel(ctx.getLayerModelPart(EntityModelLayer(Identifier(Blockround.ID, "bar_sign"), "frame")))

    override fun render(
        signEntity: BarSignBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumerProvider: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {
        val blockState = signEntity.cachedState
        matrices.push()
        matrices.translate(0.5, 0.5, 0.5)
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-(blockState.get(BarSignBlock.FACING)).asRotation()))

        matrices.push()
        val scaleFactor: Float = 2 / 3f
        matrices.scale(scaleFactor, -scaleFactor, -scaleFactor)
//        //val spriteIdentifier = TexturedRenderLayers.getSignTextureId(signType)
//
//        val spriteIdentifier = SpriteIdentifier(
//            Identifier(Blockround.ID, "block/bar_sign"),
//            Identifier(Blockround.ID, "block/bar_sign")
//        )
//        val vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumerProvider, model::getLayer)
//        model.root.render(matrices, vertexConsumer, light, overlay)
        matrices.pop()

        //textRenderer.fontHeight
        matrices.translate(
            0.0,
            -(13 + 4 / 6.0) / 64,  // (Pixels to move down + 1/2 text height) / pixels per block
            7 / 150.0  // To separate the text from the sign's surface
        )
        matrices.scale(0.010416667f, -0.010416667f, 0.010416667f)

        textRenderer.draw(
            signEntity.venueName,
            -textRenderer.getWidth(signEntity.venueName) / 2f,
            0f,
            255,
            false,
            matrices.peek().model,
            vertexConsumerProvider,
            false,
            0,
            light
        )
        matrices.pop()
    }

    override fun rendersOutsideBoundingBox(blockEntity: BarSignBlockEntity?) = true

    class BarSignModel constructor(val root: ModelPart) : Model(RenderLayer::getEntityCutoutNoCull) {
        override fun render(
            matrices: MatrixStack?,
            vertices: VertexConsumer?,
            light: Int,
            overlay: Int,
            red: Float,
            green: Float,
            blue: Float,
            alpha: Float
        ) = root.render(matrices, vertices, light, overlay, red, green, blue, alpha)
    }
}