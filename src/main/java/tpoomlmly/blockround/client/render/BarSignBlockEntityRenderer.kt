package tpoomlmly.blockround.client.render

import net.minecraft.client.font.TextRenderer
import net.minecraft.client.model.Model
import net.minecraft.client.model.ModelPart
import net.minecraft.client.model.ModelPartBuilder
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.TexturedRenderLayers
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.minecraft.util.SignType
import net.minecraft.util.math.Matrix4f
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
        matrices.push()  // push a new identity matrix onto the stack
        // Move the sign into place
        matrices.translate(0.5, 0.5, 0.5)
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-(blockState.get(BarSignBlock.FACING)).asRotation()))
        matrices.translate(-0.5, 0.0, -0.5)

        matrices.push()
        matrices.scale(1f, -1f, -1f)
        val spriteIdentifier = TexturedRenderLayers.getSignTextureId(SignType.DARK_OAK)  // TODO fix the textures
//        val spriteIdentifier = SpriteIdentifier(
//            Identifier(Blockround.ID, "block/bar_sign"),
//            Identifier(Blockround.ID, "block/bar_sign")
//        )
        val model = BarSignModel()
        val vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumerProvider, model::getLayer)
        model.root.render(matrices, vertexConsumer, light, overlay)
        matrices.pop()

        // Draw the text on the sign
        // There's a way to simplify this bit
        val textScale = 1 / 96f
        val textHeight = -(13 + 4 / 6.0) / 64  // (Pixels to move down + 1/2 text height) / pixels per block
        matrices.push()
        matrices.translate(
            0.5,
            textHeight,
            0.5 + 7 / 150.0  // To separate the text from the sign's surface and move it to the right place
        )
        matrices.scale(textScale, -textScale, textScale)
        renderText(signEntity.venueName, matrices.peek().model, vertexConsumerProvider, light)
        matrices.pop()

        // Same on the other side
        matrices.translate(
            0.5,
            textHeight,
            0.5 - 7 / 150.0
        )
        matrices.scale(-textScale, -textScale, -textScale)  // mirror and scale down
        renderText(signEntity.venueName, matrices.peek().model, vertexConsumerProvider, light)
        matrices.pop()  // pop the last matrix off the stack
    }

    override fun rendersOutsideBoundingBox(blockEntity: BarSignBlockEntity?) = true

    /**
     * Renders text on one side of the sign.
     */
    private fun renderText(
        text: Text,
        matrixModel: Matrix4f,
        vertexConsumerProvider: VertexConsumerProvider,
        light: Int
    ) = textRenderer.draw(
        text,
        -textRenderer.getWidth(text) / 2f,  // centred
        0f,
        4208437,  // dark grey
        false,
        matrixModel,
        vertexConsumerProvider,
        false,
        0,
        light
    )

    class BarSignModel : Model(RenderLayer::getEntityCutoutNoCull) {

        private val panel = ModelPart(  // The sign bit
            toCuboidList(
                ModelPartBuilder.create().cuboid(-6.5f + 8, -19f + 8, -0.5f - 8, 13f, 18f, 1f)
            ),
            mapOf()
        )
        val root = ModelPart(  // The frame
            toCuboidList(
                ModelPartBuilder.create()
                    .cuboid("right_leg", 15f, -11f, -9f, 1f, 18f, 2f)
                    .cuboid("left_leg", 0f, -11f, -9f, 1f, 18f, 2f)
                    .cuboid("crossbar", 0f, -12f, -9f, 16f, 1f, 2f)
                    .cuboid("right_foot", 15f, 7f, -11f, 1f, 1f, 6f)
                    .cuboid("left_foot", 0f, 7f, -11f, 1f, 1f, 6f)
            ),
            mapOf("panel" to panel)
        )

        companion object {
            private fun toCuboidList(builder: ModelPartBuilder): List<ModelPart.Cuboid> =
                builder.build().map { cuboidData -> cuboidData.createCuboid(16, 16) }
        }

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