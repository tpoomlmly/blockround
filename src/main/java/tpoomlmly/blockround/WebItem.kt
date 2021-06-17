package tpoomlmly.blockround

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class WebItem : Item(FabricItemSettings().group(Blockround.ROUND_ITEM_GROUP)) {

    companion object {
        private val RUNTIME = Runtime.getRuntime()
        private val OS: String = System.getProperty("os.name").lowercase()
        private val IS_WINDOWS = "win" in OS
        private val IS_MAC = "mac" in OS
        private val IS_UNIX = "nix" in OS || "nux" in OS || "aix" in OS
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (world.isClient) {
            val url = "https://fabricmc.net"
            when {
                IS_WINDOWS -> {
                    RUNTIME.exec("rundll32 url.dll,FileProtocolHandler $url")
                }
                IS_MAC -> {
                    RUNTIME.exec("open $url")
                }
                IS_UNIX -> {
                    RUNTIME.exec("xdg-open $url")
                }
            }
        }
        return super.use(world, user, hand)
    }
}