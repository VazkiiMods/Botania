package vazkii.botania.common.block.decor;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.event.ForgeEventFactory;
import vazkii.botania.common.Botania;
import vazkii.botania.common.item.block.ItemBlockMod;

public class ItemBlockBlaze extends ItemBlockMod {
    public ItemBlockBlaze(Block block, Properties props) {
        super(block, props);
    }

    @Override
    public int getBurnTime(ItemStack stack) {
        int blazeTime = ForgeEventFactory.getItemBurnTime(stack, TileEntityFurnace.getBurnTimes().getOrDefault(Items.BLAZE_ROD, 0));
        return blazeTime * (Botania.gardenOfGlassLoaded ? 5 : 10);
    }
}
