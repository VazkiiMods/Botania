/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.lens;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.helper.ForcePushHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.mixin.PistonBaseBlockAccessor;

public class ForceLens extends Lens {

	@Override
	public boolean collideBurst(ManaBurst burst, HitResult pos, boolean isManaBlock, boolean shouldKill, ItemStack stack) {
		Entity entity = burst.entity();
		if (pos.getType() == HitResult.Type.BLOCK
				&& !burst.isFake()
				&& !isManaBlock) {
			BlockHitResult rtr = (BlockHitResult) pos;
			BlockState state = entity.level().getBlockState(rtr.getBlockPos());
			ItemStack sourceLens = burst.getSourceLens();
			boolean isWarp = sourceLens.is(BotaniaItems.lensWarp);
			if (isWarp && state.is(BotaniaBlocks.pistonRelay)) {
				// warp+force should not move the force relay
				return false;
			}

			// mana burst could have been warped here, so don't assume that any block is unmovable
			moveBlocks(entity.level(), rtr.getBlockPos().relative(rtr.getDirection()), rtr.getDirection().getOpposite(), ManaBurst.NO_SOURCE);
		}

		return shouldKill;
	}

	/**
	 * Executes a force-push of blocks without an actual piston being present.
	 * 
	 * @param level            The level.
	 * @param impliedPistonPos Position where to push block from.
	 * @param direction        Direction to move block towards from the impliedPistonPos.
	 * @param pushSourcePos    Position of the push source. The block at this position is considered unmovable,
	 *                         and usually it would be the pushing piston.
	 * @return {@code true} if blocks have started moving, or {@code false} if moving blocks failed,
	 *         e.g. due to exceeded push limit or unmovable blocks being in the way.
	 */
	@SuppressWarnings("try")
	public static boolean moveBlocks(Level level, BlockPos impliedPistonPos, Direction direction, BlockPos pushSourcePos) {
		try (var ignored = new ForcePushHelper(pushSourcePos)) {
			return ((PistonBaseBlockAccessor) Blocks.PISTON).botania_moveBlocks(level, impliedPistonPos, direction, true);
		}
	}

}
