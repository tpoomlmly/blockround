package tpoomlmly.blockround.network

import net.minecraft.network.Packet
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.math.BlockPos

/**
 * Packet telling a client to open the editor for a bar sign.
 */
class BarSignEditorOpenS2CPacket constructor(val pos: BlockPos) : Packet<BlockroundClientNetworkHandler> {

    constructor(buf: PacketByteBuf) : this(buf.readBlockPos())

    override fun write(buf: PacketByteBuf) {
        buf.writeBlockPos(pos)
    }

    override fun apply(listener: BlockroundClientNetworkHandler) {
        listener.onBarSignEditorOpen(this)
    }
}