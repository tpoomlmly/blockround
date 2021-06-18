package tpoomlmly.blockround.entity

import com.mojang.brigadier.exceptions.CommandSyntaxException
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.server.command.CommandOutput
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.text.Texts
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d
import tpoomlmly.blockround.Blockround
import java.util.*

class BarSignBlockEntity(
    pos: BlockPos,
    state: BlockState
) : BlockEntity(Blockround.BAR_SIGN_BLOCK_ENTITY_TYPE, pos, state) {

    var editable = true
        set(value) {
            field = value
            if (!value) editor = null
        }
    var venueName: Text = Text.of("FUCK")
        private set
    var venueId = 0
        private set
    var editor: UUID? = null

    /**
     * Convert this sign's title and corresponding ID to NBT tags
     */
    override fun writeNbt(nbt: NbtCompound): NbtCompound {
        super.writeNbt(nbt)
        nbt.putString("venueName", Text.Serializer.toJson(venueName))
        nbt.putInt("venueId", venueId)
        return nbt
    }

    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)
        editable = false
        parseTextFromJson(nbt.getString("venueName"))
    }

    private fun parseTextFromJson(json: String): Text? {
        val unparsedText = unparsedTextFromJson(json)
        if (world is ServerWorld) {
            try {
                return Texts.parse(
                    getCommandSource(null),
                    unparsedText, null, 0
                )
            } catch (ignored: CommandSyntaxException) {
            }
        }
        return unparsedText
    }

    private fun unparsedTextFromJson(json: String): Text {
        try {
            val text: Text? = Text.Serializer.fromJson(json)
            text?.let { return text }
        } catch (e: Exception) {
        }
        return LiteralText.EMPTY
    }

    fun updateSign(name: Text, id: Int) {
        venueName = name
        venueId = id
    }

    override fun toUpdatePacket() = BlockEntityUpdateS2CPacket(
        this.pos,
        BlockEntityUpdateS2CPacket.SIGN,  // TODO replace this
        this.toInitialChunkDataNbt(),
    )

    override fun toInitialChunkDataNbt() = writeNbt(NbtCompound())

    override fun copyItemDataRequiresOperator() = true

    private fun getCommandSource(player: ServerPlayerEntity?) = ServerCommandSource(
        CommandOutput.DUMMY,
        Vec3d.ofCenter(pos),
        Vec2f.ZERO,
        world as ServerWorld?,
        2,
        player?.name?.string ?: "Bar Sign",
        player?.displayName ?: LiteralText("Sign"),
        world!!.server,
        player,
    )
}