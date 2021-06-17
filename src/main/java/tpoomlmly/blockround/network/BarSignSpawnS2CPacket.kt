package tpoomlmly.blockround.network

//import net.minecraft.network.Packet
//import net.minecraft.network.PacketByteBuf
//import net.minecraft.network.listener.ClientPlayPacketListener
//import net.minecraft.util.math.BlockPos
//import net.minecraft.util.math.Direction
//import tpoomlmly.blockround.entity.BarSignEntity
//import java.util.*
//
//class BarSignSpawnS2CPacket : Packet<ClientPlayPacketListener> {
//
//    val id: Int
//    val uuid: UUID
//    val pos: BlockPos
//    val facing: Direction
//    val width: Int
//
//    constructor(entity: BarSignEntity) {
//        id = entity.id
//        uuid = entity.uuid
//        pos = entity.decorationBlockPos
//        facing = entity.horizontalFacing
//        width = entity.width
//    }
//
//    constructor(buf: PacketByteBuf) {
//        id = buf.readVarInt()
//        uuid = buf.readUuid()
//        width = buf.readVarInt()
//        pos = buf.readBlockPos()
//        facing = Direction.fromHorizontal(buf.readUnsignedByte().toInt())
//    }
//
//    override fun write(buf: PacketByteBuf) {
//        buf.writeVarInt(id)
//        buf.writeUuid(uuid)
//        buf.writeVarInt(width)
//        buf.writeBlockPos(pos)
//        buf.writeByte(facing.horizontal)
//    }
//
//    override fun apply(clientPlayPacketListener: ClientPlayPacketListener) {
////        TODO("not done yet")
////        S2CPlayChannelEvents
//        clientPlayPacketListener.
//    }
//
//    fun getBarSignUuid() = uuid
//}