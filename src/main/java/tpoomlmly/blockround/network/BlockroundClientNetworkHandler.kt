package tpoomlmly.blockround.network

import net.minecraft.client.MinecraftClient
import net.minecraft.client.world.ClientWorld
import net.minecraft.network.ClientConnection
import net.minecraft.network.NetworkThreadUtils
import net.minecraft.network.listener.PacketListener
import net.minecraft.text.Text
import tpoomlmly.blockround.entity.BarSignBlockEntity

class BlockroundClientNetworkHandler constructor(private val client: MinecraftClient, private val world: ClientWorld) : PacketListener {

    override fun onDisconnected(reason: Text?) {
        TODO("Not yet implemented")
    }

    override fun getConnection(): ClientConnection {
        TODO("Not yet implemented")
    }

    /**
     * Open the edit screen on the client.
     */
    fun onBarSignEditorOpen(packet: BarSignEditorOpenS2CPacket) {
        NetworkThreadUtils.forceMainThread(packet, this, client)
        val blockPos = packet.pos
        var barSignBlockEntity = world.getBlockEntity(blockPos)
        if (barSignBlockEntity !is BarSignBlockEntity) {
            barSignBlockEntity = BarSignBlockEntity(blockPos, world.getBlockState(blockPos))
            barSignBlockEntity.world = world
            client.world
        }

        println("open the editor")
        // client.player.openBarSignEditor(barSignBlockEntity as BarSignBlockEntity)
    }
}