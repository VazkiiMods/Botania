package vazkii.botania.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockWithMetaNameAndColor extends ItemBlockWithMetadataAndName {
    public ItemBlockWithMetaNameAndColor(Block block) {
        super(block);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass)
    {
        return this.block.getRenderColor(this.block.getStateFromMeta(stack.getMetadata()));
    }
}
