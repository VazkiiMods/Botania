/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */

package vazkii.botania.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import vazkii.botania.common.lib.BotaniaTags;

public class HornOfTheCoveringItem extends HornItem {

	public static final int NUM_BLOCKS_TO_BREAK = 64;
	public static final int RANGE = 6;
	public static final int RANGE_Y = 11;

	public HornOfTheCoveringItem(Properties props) {
		super(props);
	}

	@Override
	protected boolean canHarvest(Level level, BlockPos pos) {
		return level.getBlockState(pos).is(BotaniaTags.Blocks.HORN_OF_THE_COVERING_BREAKABLE);
	}

	protected int getRange() {
		return RANGE;
	}

	protected int getRangeY() {
		return RANGE_Y;
	}

	protected int getNumBlocksToBreak() {
		return NUM_BLOCKS_TO_BREAK;
	}

	@Override
	public ItemStack getRandomDropTool() {
		return Items.DIAMOND_SHOVEL.getDefaultInstance();
	}

}
