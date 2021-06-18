package tpoomlmly.blockround

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.event.player.AttackBlockCallback
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.ActionResult
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import tpoomlmly.blockround.block.BarSignBlock
import tpoomlmly.blockround.client.ui.BarSignEditor
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

        registerBlock(BAR_SIGN_BLOCK, "bar_sign_block")

        registerItem(BAR_SIGN_BLOCK_ITEM, "bar_sign_block_item")
        registerItem(WEB_ITEM, "web_item")
//        Registry.register(Registry.ITEM, Identifier(ID, "bar_sign_item"), BAR_SIGN_ITEM)

        //Registry.register(Registry.ENTITY_TYPE, Identifier(ID, BarSignEntityType.ID), BAR_SIGN_ENTITY_TYPE)

        AttackBlockCallback.EVENT.register { _, world, _, pos, _ ->
            if (world.getBlockEntity(
                    pos,
                    BAR_SIGN_BLOCK_ENTITY_TYPE
                ).isPresent
            ) println("fuck") else println("ohno")
            ActionResult.PASS
        }

        ServerPlayNetworking.registerGlobalReceiver(Identifier(ID, "barsign")) { server, player, handler, buf, sender ->

        }

        ClientPlayNetworking.registerGlobalReceiver(Identifier(ID, "barsign")) { client, handler, buf, responseSender ->
            client.execute {
                println("barsign")
                BarSignEditor.open()
            }
        }
    }

    private fun registerBlock(block: Block, name: String) =
        Registry.register(Registry.BLOCK, Identifier(ID, name), block)

    private fun registerItem(item: Item, name: String) = Registry.register(Registry.ITEM, Identifier(ID, name), item)
}