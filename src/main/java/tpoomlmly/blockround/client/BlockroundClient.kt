package tpoomlmly.blockround.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import tpoomlmly.blockround.Blockround
import tpoomlmly.blockround.client.ui.BarSignEditor

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
class BlockroundClient : ClientModInitializer {
    override fun onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(Blockround.BAR_SIGN_CHANNEL) { client, handler, buf, responseSender ->
            val blockPos = buf.readBlockPos()
            client.execute {
                println("barsign")
                println(blockPos)
                BarSignEditor.open()
            }
        }
    }
}