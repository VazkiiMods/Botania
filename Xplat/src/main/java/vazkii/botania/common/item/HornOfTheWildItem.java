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
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.common.lib.BotaniaTags;

public class HornOfTheWildItem extends HornItem {

	public static final int NUM_BLOCKS_TO_BREAK = 32;
	public static final int RANGE = 12;
	public static final int RANGE_Y = 3;

	public HornOfTheWildItem(Properties props) {
		super(props);
	}

	@Override
	protected boolean canHarvest(Level level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		return (state.getBlock() instanceof BushBlock && !state.is(BotaniaTags.Blocks.SPECIAL_FLOWERS)
				|| state.is(BotaniaTags.Blocks.HORN_OF_THE_WILD_BREAKABLE)) && !state.is(BotaniaTags.Blocks.HORN_OF_THE_WILD_IMMUNE);
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
