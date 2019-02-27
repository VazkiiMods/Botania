package vazkii.botania.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.recipe.IElvenItem;

public class ItemBlockDreamwood extends ItemBlockMod implements IElvenItem {

	public ItemBlockDreamwood(Block block, Properties props) {
		super(block, props);
	}

	@Override
	public boolean isElvenItem(ItemStack stack) {
		return true;
	}

}
