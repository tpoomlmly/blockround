package tpoomlmly.blockround.client.render

import net.minecraft.client.font.TextRenderer
import net.minecraft.client.model.Model
import net.minecraft.client.model.ModelPart
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.render.entity.model.EntityModelLayer
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3f
import tpoomlmly.blockround.Blockround
import tpoomlmly.blockround.block.BarSignBlock
import tpoomlmly.blockround.entity.BarSignBlockEntity

class BarSignBlockEntityRenderer(ctx: BlockEntityRendererFactory.Context) : BlockEntityRenderer<BarSignBlockEntity> {

    private val textRenderer: TextRenderer = ctx.textRenderer
    private val model: BarSignModel =
        BarSignModel(ctx.getLayerModelPart(EntityModelLayer(Identifier(Blockround.ID, "bar_sign"), "main")))

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
        val scaleFactor: Float = 2 / 3F
        matrices.scale(scaleFactor, -scaleFactor, -scaleFactor)
        //val spriteIdentifier = TexturedRenderLayers.getSignTextureId(signType)

        val spriteIdentifier = SpriteIdentifier(
            Identifier(Blockround.ID, "block/bar_sign"),
            Identifier(Blockround.ID, "block/bar_sign")
        )
        val vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumerProvider, model::getLayer)
        model.root.render(matrices, vertexConsumer, light, overlay)
        matrices.pop()

        this.textRenderer.draw(
            signEntity.venueName,
            -textRenderer.getWidth(signEntity.venueName) / 2F,
            -20F,
            0,
            true,
            matrices.peek().model,
            vertexConsumerProvider,
            false,
            0,
            light
        )
        matrices.pop()
    }

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