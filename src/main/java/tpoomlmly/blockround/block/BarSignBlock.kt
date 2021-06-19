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
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView
import tpoomlmly.blockround.entity.BarSignBlockEntity

class BarSignBlock : AbstractSignBlock(
    FabricBlockSettings.of(Material.DECORATION).noCollision().strength(1F).sounds(BlockSoundGroup.WOOD),
    SignType.OAK
) {
    companion object {
        val FACING: DirectionProperty = HorizontalFacingBlock.FACING
        private val OUTLINES = mapOf(
            Direction.NORTH to createCuboidShape(1.5, 0.5, 7.5, 14.5, 19.0, 8.5),
            Direction.SOUTH to createCuboidShape(1.5, 0.5, 7.5, 14.5, 19.0, 8.5),
            Direction.EAST to createCuboidShape(7.5, 0.5, 1.5, 8.5, 19.0, 14.5),
            Direction.WEST to createCuboidShape(7.5, 0.5, 1.5, 8.5, 19.0, 14.5),
        )
    }

    init {
        this.defaultState =
            this.stateManager.defaultState
                .with(FACING, Direction.NORTH)
                .with(WATERLOGGED, false)
    }

    /**
     * Gets the shape of the outline that appears when someone looks at this block.
     */
    override fun getOutlineShape(state: BlockState, world: BlockView?, pos: BlockPos?, context: ShapeContext?) =
        OUTLINES[state.get(FACING)]

    /**
     * Can this sign be placed at the given position?
     * @return true if the block underneath is solid.
     */
    override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos) =
        world.getBlockState(pos.down()).material.isSolid

    /**
     * Calculates the state of this sign when it's placed.
     */
    override fun getPlacementState(context: ItemPlacementContext): BlockState? {
        val directions = context.placementDirections.filter { it.axis.isHorizontal }
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

    /**
     * Gets the new state for this block when a neighbour has updated.
     */
    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState?,
        world: WorldAccess?,
        pos: BlockPos?,
        neighborPos: BlockPos?
    ): BlockState? = if (direction == Direction.DOWN && !state.canPlaceAt(world, pos))
        Blocks.AIR.defaultState
    else super.getStateForNeighborUpdate(
        state,
        direction,
        neighborState,
        world,
        pos,
        neighborPos
    )

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState =
        state.with(FACING, rotation.rotate(state.get(FACING)))

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState =
        state.rotate(mirror.getRotation(state.get(FACING)))

    override fun appendProperties(builder: StateManager.Builder<Block?, BlockState?>) {
        builder.add(FACING, WATERLOGGED)
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState) = BarSignBlockEntity(pos, state)

    override fun canMobSpawnInside() = false
}
