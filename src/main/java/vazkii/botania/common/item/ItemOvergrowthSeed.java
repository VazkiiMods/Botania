/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;

public class ItemOvergrowthSeed extends Item {

	public ItemOvergrowthSeed(Settings props) {
		super(props);
	}

	@Nonnull
	@Override
	public ActionResult useOnBlock(ItemUsageContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getBlockPos();

		BlockState state = world.getBlockState(pos);
		if (state.getBlock() == Blocks.GRASS_BLOCK) {
			if (!world.isClient) {
				world.syncWorldEvent(2001, pos, Block.getRawIdFromState(state));
				world.setBlockState(pos, ModBlocks.enchantedSoil.getDefaultState());
				ctx.getStack().decrement(1);
			}
			return ActionResult.SUCCESS;
		}
		return ActionResult.PASS;
	}

}
