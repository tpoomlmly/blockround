package tpoomlmly.blockround

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.block.entity.BlockEntityType
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

        @JvmField
        val ROUND_ITEM_GROUP: ItemGroup =
            FabricItemGroupBuilder.build(Identifier(ID, "round_item_group")) { ItemStack(Items.PAINTING) }

//        @JvmField
//        val BAR_SIGN_ENTITY_TYPE = BarSignEntityType()

        @JvmField
        val BAR_SIGN_BLOCK = BarSignBlock()

        @JvmField
        val BAR_SIGN_BLOCK_ENTITY_TYPE: BlockEntityType<BarSignBlockEntity> =
            FabricBlockEntityTypeBuilder.create(::BarSignBlockEntity, BAR_SIGN_BLOCK).build(null)

        @JvmField
        val WEB_ITEM = WebItem()

//        @JvmField
//        val BAR_SIGN_ITEM = BarSignItem()

        @JvmField
        val BAR_SIGN_BLOCK_ITEM = BarSignBlockItem()
    }

    override fun onInitialize() {
        Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            Identifier(ID, "bar_sign_block_entity_type"),
            BAR_SIGN_BLOCK_ENTITY_TYPE
        )

        Registry.register(Registry.ITEM, Identifier(ID, "web_item"), WEB_ITEM)
//        Registry.register(Registry.ITEM, Identifier(ID, "bar_sign_item"), BAR_SIGN_ITEM)

        //Registry.register(Registry.ENTITY_TYPE, Identifier(ID, BarSignEntityType.ID), BAR_SIGN_ENTITY_TYPE)
    }
}