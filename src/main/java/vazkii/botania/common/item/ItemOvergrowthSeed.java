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
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;

public class ItemOvergrowthSeed extends Item {

	public ItemOvergrowthSeed(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public ActionResultType onItemUse(ItemUseContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getPos();

		// todo 1.13 grass tag
		BlockState state = world.getBlockState(pos);
		if (state.getBlock() == Blocks.GRASS_BLOCK) {
			if (!world.isRemote) {
				world.playEvent(2001, pos, Block.getStateId(state));
				world.setBlockState(pos, ModBlocks.enchantedSoil.getDefaultState());
				ctx.getItem().shrink(1);
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

}
