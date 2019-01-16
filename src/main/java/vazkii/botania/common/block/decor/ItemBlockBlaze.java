package vazkii.botania.common.block.decor;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import vazkii.botania.common.Botania;
import vazkii.botania.common.item.block.ItemBlockMod;

public class ItemBlockBlaze extends ItemBlockMod {
    public ItemBlockBlaze(Block block) {
        super(block);
    }

    @Override
    public int getItemBurnTime(ItemStack stack) {
        return TileEntityFurnace.getItemBurnTime(new ItemStack(Items.BLAZE_ROD))
                * (Botania.gardenOfGlassLoaded ? 5 : 10);
    }
}
