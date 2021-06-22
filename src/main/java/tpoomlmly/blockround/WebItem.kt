package tpoomlmly.blockround

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.Util
import net.minecraft.world.World

class WebItem : Item(FabricItemSettings().group(Blockround.ROUND_ITEM_GROUP)) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (world.isClient) Util.getOperatingSystem().open("https://fabricmc.net")
        return super.use(world, user, hand)
    }
}