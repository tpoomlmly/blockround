package tpoomlmly.blockround

import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

class Blockround : ModInitializer {

    companion object {
        @JvmField
        val WEB_ITEM = WebItem()
    }

    override fun onInitialize() {
        Registry.register(Registry.ITEM, Identifier("blockround", "web_item"), WEB_ITEM)
    }
}