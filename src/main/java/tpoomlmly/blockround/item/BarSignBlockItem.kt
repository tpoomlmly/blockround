package tpoomlmly.blockround.item

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.entity.SignBlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import tpoomlmly.blockround.Blockround

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
        pos: BlockPos?,
        world: World,
        player: PlayerEntity?,
        stack: ItemStack?,
        state: BlockState?
    ): Boolean {
        val postPlacementSuccess = super.postPlacement(pos, world, player, stack, state)
        if (!world.isClient && !postPlacementSuccess && player != null) {
            player.openEditSignScreen(world.getBlockEntity(pos) as SignBlockEntity?)
            // TODO player.openEditSignScreen(world.getBlockEntity(pos) as BarSignBlockEntity?)
        }
        return postPlacementSuccess
    }
}
