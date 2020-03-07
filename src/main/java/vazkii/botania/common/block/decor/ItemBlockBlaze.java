package vazkii.botania.common.block.decor;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.ForgeHooks;
import vazkii.botania.common.Botania;

public class ItemBlockBlaze extends BlockItem {
    public ItemBlockBlaze(Block block, Properties props) {
        super(block, props);
    }

    @Override
    public int getBurnTime(ItemStack stack) {
        int blazeTime = ForgeHooks.getBurnTime(new ItemStack(Items.BLAZE_ROD));
        return blazeTime * (Botania.gardenOfGlassLoaded ? 5 : 10);
    }
}
