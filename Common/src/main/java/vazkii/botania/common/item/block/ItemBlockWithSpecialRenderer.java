package vazkii.botania.common.item.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

/**
 * Mixed in from Forge to register the special renderer.
 * Why is this not just a normal map from item to renderer ?????
 * No functional purpose on Fabric.
 */
public class ItemBlockWithSpecialRenderer extends BlockItem {
	public ItemBlockWithSpecialRenderer(Block block, Properties props) {
		super(block, props);
	}
}
