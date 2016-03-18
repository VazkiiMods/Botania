package vazkii.botania.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.common.item.IColorable;

public class ItemBlockWithMetaNameAndColor extends ItemBlockWithMetadataAndName implements IColorable {
    public ItemBlockWithMetaNameAndColor(Block block) {
        super(block);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        return Minecraft.getMinecraft().getBlockColors()
                .colorMultiplier(this.block.getStateFromMeta(stack.getMetadata()), null, null, renderPass);
    }
}
