package tpoomlmly.blockround.network

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.math.BlockPos

fun barSignEditorOpenS2CBuffer(pos: BlockPos): PacketByteBuf = PacketByteBufs.create().writeBlockPos(pos)

fun barSignUpdateC2SBuffer(pos: BlockPos, venueName: String, venueId: Int): PacketByteBuf =
    PacketByteBufs.create().writeBlockPos(pos).writeString(venueName).writeVarInt(venueId)