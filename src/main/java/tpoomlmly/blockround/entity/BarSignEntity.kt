package tpoomlmly.blockround.entity

//import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
//import net.minecraft.entity.Entity
//import net.minecraft.entity.EntityType
//import net.minecraft.entity.decoration.AbstractDecorationEntity
//import net.minecraft.entity.decoration.painting.PaintingEntity
//import net.minecraft.entity.player.PlayerEntity
//import net.minecraft.item.ItemStack
//import net.minecraft.item.Items
//import net.minecraft.nbt.NbtCompound
//import net.minecraft.network.Packet
//import net.minecraft.sound.SoundEvents
//import net.minecraft.util.math.BlockPos
//import net.minecraft.util.math.Direction
//import net.minecraft.world.GameRules
//import net.minecraft.world.World
//import tpoomlmly.blockround.Blockround
//
//class BarSignEntity : AbstractDecorationEntity {
//
//    var width: Int = 3
//        private set
//
//    constructor(entityType: EntityType<out PaintingEntity>, world: World) : super(entityType, world)
//
//    constructor(world: World, pos: BlockPos, direction: Direction) : super(
//        Blockround.BAR_SIGN_ENTITY_TYPE,
//        world,
//        pos
//    ) {
//        val validWidths = (6 downTo 3).dropWhile {
//            this.width = it
//            this.canStayAttached()
//        }  // filter out the widths where the sign wouldn't fit on the wall
//
//        if (validWidths.isNotEmpty()) width = validWidths.random()
//
//        this.setFacing(direction)
//    }
//
//    constructor(world: World, pos: BlockPos, direction: Direction, width: Int) : this(world, pos, direction) {
//        this.width = width
//        this.setFacing(direction)
//    }
//
//    override fun writeCustomDataToNbt(nbt: NbtCompound) {
//        nbt.putInt("Width", this.width)
//        nbt.putByte("Facing", facing.horizontal.toByte())
//        super.writeCustomDataToNbt(nbt)
//    }
//
//    override fun readCustomDataFromNbt(nbt: NbtCompound) {
//        this.width = nbt.getInt("Width")
//        facing = Direction.fromHorizontal(nbt.getByte("Facing").toInt())
//        super.readCustomDataFromNbt(nbt)
//        setFacing(facing)
//    }
//
//    override fun getWidthPixels() = width * 16
//
//    override fun getHeightPixels() = 16
//
//    override fun onBreak(entity: Entity?) {
//        if (world.gameRules.getBoolean(GameRules.DO_ENTITY_DROPS)) {
//            this.playSound(SoundEvents.ENTITY_PAINTING_BREAK, 1F, 1F)
//            if (entity is PlayerEntity) {
//                if (entity.abilities.creativeMode) {
//                    return
//                }
//            }
//
//            this.dropItem(Blockround.BAR_SIGN_ITEM)
//        }
//    }
//
//    override fun onPlace() = this.playSound(SoundEvents.ENTITY_PAINTING_PLACE, 1F, 1F)
//
//    override fun refreshPositionAndAngles(x: Double, y: Double, z: Double, yaw: Float, pitch: Float) {
//        this.setPosition(x, y, z)
//    }
//
//    override fun updateTrackedPositionAndAngles(
//        x: Double,
//        y: Double,
//        z: Double,
//        yaw: Float,
//        pitch: Float,
//        interpolationSteps: Int,
//        interpolate: Boolean
//    ) {
//        val blockPos = attachmentPos.add(x - x, y - y, z - z)
//        this.setPosition(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble())
//    }
//
//    override fun createSpawnPacket(): Packet<*> {
//        return ServerPlayNetworking.createS2CPacket(Identifier.)
//        //return BarSignSpawnS2CPacket(this)
//    }
//
//    override fun getPickBlockStack() = ItemStack(Items.PAINTING)
//}