package tpoomlmly.blockround.item

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import tpoomlmly.blockround.Blockround
import tpoomlmly.blockround.entity.BarSignBlockEntity
import tpoomlmly.blockround.network.barSignEditorOpenS2CBuffer

class BarSignBlockItem : BlockItem(
    Blockround.BAR_SIGN_BLOCK,
    FabricItemSettings().maxCount(8).group(Blockround.ROUND_ITEM_GROUP)
) {
    override fun getPlacementState(context: ItemPlacementContext): BlockState? {
        val blockState: BlockState? = this.block.getPlacementState(context)
        var placementState: BlockState? = null

        for (direction in context.placementDirections) {
            if (direction != Direction.UP && blockState?.canPlaceAt(context.world, context.blockPos) == true) {
                placementState = blockState
                break
            }
        }
        return if (placementState != null && context.world.canPlace(
                placementState,
                context.blockPos,
                ShapeContext.absent()
            )
        ) placementState else null
    }

    override fun appendBlocks(map: MutableMap<Block?, Item?>, item: Item?) {
        super.appendBlocks(map, item)
        map[this.block] = item
    }

    override fun postPlacement(
        pos: BlockPos,
        world: World,
        player: PlayerEntity?,
        stack: ItemStack?,
        state: BlockState?
    ): Boolean {
        val postPlacementSuccess = super.postPlacement(pos, world, player, stack, state)
        if (!world.isClient && !postPlacementSuccess && player != null) {
            // Set the player as the sign's editor
            (world.getBlockEntity(pos) as BarSignBlockEntity).editor = player.uuid
            // Tell the client to open the edit sign screen
            val serverPlayer = player as ServerPlayerEntity
            serverPlayer.networkHandler.sendPacket(BlockUpdateS2CPacket(world, pos))
            ServerPlayNetworking.send(serverPlayer, Blockround.BAR_SIGN_CHANNEL, barSignEditorOpenS2CBuffer(pos))
        }
        return postPlacementSuccess
    }
}
