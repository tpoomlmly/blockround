package tpoomlmly.blockround

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import tpoomlmly.blockround.block.BarSignBlock
import tpoomlmly.blockround.entity.BarSignBlockEntity
import tpoomlmly.blockround.item.BarSignBlockItem

class Blockround : ModInitializer {

    companion object {
        const val ID = "blockround"
        val BAR_SIGN_CHANNEL = Identifier(ID, "barsign")

        @JvmField
        val ROUND_ITEM_GROUP: ItemGroup =
            FabricItemGroupBuilder.build(Identifier(ID, "round_item_group")) { ItemStack(Items.PAINTING) }

        @JvmField
        val BAR_SIGN_BLOCK = BarSignBlock()

        @JvmField
        val BAR_SIGN_BLOCK_ENTITY_TYPE: BlockEntityType<BarSignBlockEntity> =
            FabricBlockEntityTypeBuilder.create(::BarSignBlockEntity, BAR_SIGN_BLOCK).build(null)

        @JvmField
        val WEB_ITEM = WebItem()

        @JvmField
        val BAR_SIGN_BLOCK_ITEM = BarSignBlockItem()
    }

    override fun onInitialize() {
        Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            Identifier(ID, "bar_sign_block_entity_type"),
            BAR_SIGN_BLOCK_ENTITY_TYPE
        )

        Registry.register(Registry.BLOCK, Identifier(ID, "bar_sign_block"), BAR_SIGN_BLOCK)

        registerItem(BAR_SIGN_BLOCK_ITEM, "bar_sign_block_item")
        registerItem(WEB_ITEM, "web_item")
    }

    private fun registerItem(item: Item, name: String) = Registry.register(Registry.ITEM, Identifier(ID, name), item)
}