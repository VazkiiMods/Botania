/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;

public class ItemOvergrowthSeed extends Item {

	public ItemOvergrowthSeed(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Level world = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();

		BlockState state = world.getBlockState(pos);
		if (state.getBlock() == Blocks.GRASS_BLOCK) {
			if (!world.isClientSide) {
				world.levelEvent(2001, pos, Block.getId(state));
				world.setBlockAndUpdate(pos, ModBlocks.enchantedSoil.defaultBlockState());
				ctx.getItemInHand().shrink(1);
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

}
