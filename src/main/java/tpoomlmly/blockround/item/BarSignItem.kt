package tpoomlmly.blockround.item

//import net.fabricmc.fabric.api.item.v1.FabricItemSettings
//import net.minecraft.entity.EntityType
//import net.minecraft.item.DecorationItem
//import net.minecraft.item.ItemUsageContext
//import net.minecraft.util.ActionResult
//import net.minecraft.world.event.GameEvent
//import tpoomlmly.blockround.Blockround
//import tpoomlmly.blockround.entity.BarSignEntity
//
//class BarSignItem :
//    DecorationItem(Blockround.BAR_SIGN_ENTITY_TYPE, FabricItemSettings().group(Blockround.ROUND_ITEM_GROUP)) {
//    override fun useOnBlock(context: ItemUsageContext): ActionResult? {
//        val blockPos = context.blockPos  // the block the item was used on
//        val direction = context.side  // the side of the block the item was used on
//        val barSignPos = blockPos.offset(direction)  // the space where the bar sign should go
//        val playerEntity = context.player
//        val itemStack = context.stack
//
//        return if (playerEntity != null && !canPlaceOn(playerEntity, direction, itemStack, barSignPos)) {
//            ActionResult.FAIL
//        } else {
//            val world = context.world
//            val barSignEntity = BarSignEntity(world, barSignPos, direction)
//            val nbtCompound = itemStack.tag
//
//            if (nbtCompound != null) {
//                EntityType.loadFromEntityNbt(world, playerEntity, barSignEntity, nbtCompound)
//            }
//
//            if (barSignEntity.canStayAttached()) {
//                if (!world.isClient) {
//                    barSignEntity.onPlace()
//                    world.emitGameEvent(playerEntity, GameEvent.ENTITY_PLACE, blockPos)
//                    world.spawnEntity(barSignEntity)
//                }
//                itemStack.decrement(1)
//                ActionResult.success(world.isClient)
//            } else {
//                ActionResult.CONSUME
//            }
//        }
//    }
//}