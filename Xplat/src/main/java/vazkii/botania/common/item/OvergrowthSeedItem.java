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
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.block.BotaniaBlocks;

public class OvergrowthSeedItem extends Item {

	public OvergrowthSeedItem(Properties props) {
		super(props);
	}

	@NotNull
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Level world = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();

		BlockState state = world.getBlockState(pos);
		if (state.is(Blocks.GRASS_BLOCK)) {
			if (!world.isClientSide) {
				world.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
				world.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
				world.setBlockAndUpdate(pos, BotaniaBlocks.enchantedSoil.defaultBlockState());
				ctx.getItemInHand().shrink(1);
			}
			return InteractionResult.sidedSuccess(world.isClientSide());
		}
		return InteractionResult.PASS;
	}

}
