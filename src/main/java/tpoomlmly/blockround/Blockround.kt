package tpoomlmly.blockround

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

class Blockround : ModInitializer {

    companion object {
        @JvmField
        val WEB_ITEM = Item(FabricItemSettings().group(ItemGroup.MISC))
    }
    override fun onInitialize() {
        Registry.register(Registry.ITEM, Identifier("blockround", "web_item"), WEB_ITEM)
    }
}