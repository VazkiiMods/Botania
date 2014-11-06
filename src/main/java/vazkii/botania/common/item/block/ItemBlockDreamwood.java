package vazkii.botania.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.recipe.IElvenItem;

public class ItemBlockDreamwood extends ItemBlockWithMetadataAndName implements IElvenItem {

	public ItemBlockDreamwood(Block block) {
		super(block);
	}

	@Override
	public boolean isElvenItem(ItemStack stack) {
		return true;
	}

}
