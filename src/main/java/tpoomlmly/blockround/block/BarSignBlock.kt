package tpoomlmly.blockround.block

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.state.StateManager
import net.minecraft.state.property.DirectionProperty
import net.minecraft.util.*
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView

class BarSignBlock : AbstractSignBlock(
    FabricBlockSettings.of(Material.WOOD).noCollision().strength(1F).sounds(BlockSoundGroup.WOOD),
    SignType.OAK
) {
    companion object {
        val FACING: DirectionProperty = HorizontalFacingBlock.FACING
        private val FACING_TO_SHAPE = mapOf(
            Direction.NORTH to createCuboidShape(0.0, 4.5, 14.0, 16.0, 12.5, 16.0),
            Direction.SOUTH to createCuboidShape(0.0, 4.5, 0.0, 16.0, 12.5, 2.0),
            Direction.EAST to createCuboidShape(0.0, 4.5, 0.0, 2.0, 12.5, 16.0),
            Direction.WEST to createCuboidShape(14.0, 4.5, 0.0, 16.0, 12.5, 16.0),
        )
    }

    init {
        this.defaultState =
            this.stateManager.defaultState.with(FACING, Direction.NORTH)
                .with(WATERLOGGED, false)
    }

    override fun getTranslationKey(): String = asItem().translationKey

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape? {
        return FACING_TO_SHAPE[state.get(FACING)]
    }

    override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos): Boolean {
        return world.getBlockState(pos.offset(state.get(FACING).opposite)).material.isSolid
    }

    override fun getPlacementState(context: ItemPlacementContext): BlockState? {
        var blockState = defaultState

        for (direction in context.placementDirections) {
            if (direction.axis.isHorizontal) {
                blockState = blockState.with(FACING, direction.opposite)

                if (blockState.canPlaceAt(context.world, context.blockPos)) return blockState.with(
                    WATERLOGGED,
                    context.world.getFluidState(context.blockPos).fluid === Fluids.WATER
                )
            }
        }

        return null
    }

    /**
     * AbstractSignBlock tries to dye the sign. This is the same as the superclass but without dye logic.
     */
    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hitResult: BlockHitResult
    ) = if (world.isClient()) ActionResult.CONSUME else ActionResult.PASS

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState?,
        world: WorldAccess?,
        pos: BlockPos?,
        neighborPos: BlockPos?
    ): BlockState? {
        return if (direction.opposite == state.get(FACING) && !state.canPlaceAt(world, pos))
            Blocks.AIR.defaultState
        else super.getStateForNeighborUpdate(
            state,
            direction,
            neighborState,
            world,
            pos,
            neighborPos
        )
    }

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
        return state.with(FACING, rotation.rotate(state.get(FACING)))
    }

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState? {
        return state.rotate(mirror.getRotation(state.get(FACING)))
    }

    override fun appendProperties(builder: StateManager.Builder<Block?, BlockState?>) {
        builder.add(FACING, WATERLOGGED)
    }
}
