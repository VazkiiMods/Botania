package vazkii.botania.common.block.decor;

import net.minecraft.block.Block;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import vazkii.botania.common.Botania;
import vazkii.botania.common.item.block.ItemBlockMod;

public class ItemBlockBlaze extends ItemBlockMod {
    public ItemBlockBlaze(Block block, Properties props) {
        super(block, props);
    }

    @Override
    public int getBurnTime(ItemStack stack) {
        int blazeTime = ForgeHooks.getBurnTime(new ItemStack(Items.BLAZE_ROD));
        return blazeTime * (Botania.gardenOfGlassLoaded ? 5 : 10);
    }
}
