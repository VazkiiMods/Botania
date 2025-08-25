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

public class HornOfTheCanopyItem extends HornItem {

	public static final int NUM_BLOCKS_TO_BREAK = 48;
	public static final int RANGE = 9;
	public static final int RANGE_Y = 7;

	public HornOfTheCanopyItem(Properties props) {
		super(props);
	}

	@Override
	protected boolean canHarvest(Level level, BlockPos pos) {
		return level.getBlockState(pos).is(BotaniaTags.Blocks.HORN_OF_THE_CANOPY_BREAKABLE);
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
		return Items.SHEARS.getDefaultInstance();
	}

}
