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
import tpoomlmly.blockround.entity.BarSignBlockEntity

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

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape? {
        return FACING_TO_SHAPE[state.get(FACING)]
    }

    override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos): Boolean {
        return world.getBlockState(pos.down()).material.isSolid
    }

    /**
     * Calculates the state of this sign when it's placed.
     */
    override fun getPlacementState(context: ItemPlacementContext): BlockState? {
        val directions = context.placementDirections.filter {dir -> dir.axis.isHorizontal}
        return if (directions.isEmpty()) null
        else defaultState
            .with(FACING, directions[0].opposite)
            .with(WATERLOGGED, context.world.getFluidState(context.blockPos).fluid === Fluids.WATER)
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
    ) = if (world.isClient()) ActionResult.CONSUME else ActionResult.SUCCESS

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState?,
        world: WorldAccess?,
        pos: BlockPos?,
        neighborPos: BlockPos?
    ): BlockState? {
        return if (direction == Direction.DOWN && !state.canPlaceAt(world, pos))
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

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState {
        return state.rotate(mirror.getRotation(state.get(FACING)))
    }

    override fun appendProperties(builder: StateManager.Builder<Block?, BlockState?>) {
        builder.add(FACING, WATERLOGGED)
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState) = BarSignBlockEntity(pos, state)

    override fun canMobSpawnInside() = false
}
