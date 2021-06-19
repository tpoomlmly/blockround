package tpoomlmly.blockround.client.ui

import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.TranslatableText
import tpoomlmly.blockround.entity.BarSignBlockEntity

class BarSignEditor constructor(private val barSign: BarSignBlockEntity) : Screen(TranslatableText("barsign.edit")) {
    companion object {
        fun open() {
            println("open the editor")
        }
    }
}