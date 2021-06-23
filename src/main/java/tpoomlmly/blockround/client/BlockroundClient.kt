package tpoomlmly.blockround.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry
import tpoomlmly.blockround.Blockround
import tpoomlmly.blockround.client.render.BarSignBlockEntityRenderer
import tpoomlmly.blockround.client.ui.BarSignEditor
import tpoomlmly.blockround.entity.BarSignBlockEntity

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
class BlockroundClient : ClientModInitializer {
    override fun onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(Blockround.BAR_SIGN_CHANNEL) { client, _, buf, _ ->
            val blockPos = buf.readBlockPos()
            client.execute {
                client.world?.getBlockEntity(blockPos)
                    ?.let { client.openScreen(BarSignEditor(it as BarSignBlockEntity)) }
            }
        }

        BlockEntityRendererRegistry.INSTANCE.register(
            Blockround.BAR_SIGN_BLOCK_ENTITY_TYPE,
            ::BarSignBlockEntityRenderer
        )
    }
}