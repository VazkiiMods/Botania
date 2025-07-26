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
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.xplat.XplatAbstractions;

public class KindleLens extends Lens {
	@Override
	public void updateBurst(ManaBurst burst, ItemStack stack) {
		Entity entity = burst.entity();
		if (!entity.level().isClientSide) {
			entity.setSecondsOnFire(3);
		}
	}

	@Override
	public boolean collideBurst(ManaBurst burst, HitResult rtr, boolean isManaBlock, boolean shouldKill, ItemStack stack) {
		Entity entity = burst.entity();

		if (!entity.level().isClientSide && rtr.getType() == HitResult.Type.BLOCK
				&& !burst.isFake() && !isManaBlock) {
			BlockHitResult brtr = (BlockHitResult) rtr;
			BlockPos pos = brtr.getBlockPos();
			Direction dir = brtr.getDirection();

			BlockPos offPos = pos.relative(dir);
			Level level = entity.level();

			BlockState stateAt = level.getBlockState(pos);
			BlockState stateAtOffset = level.getBlockState(offPos);

			// Get player for event firing
			var player = XplatAbstractions.INSTANCE.getPlayer(level, burst.getShooterUUID(), getClass().getName());

			// Handle nether portal removal
			if (stateAt.is(Blocks.NETHER_PORTAL)) {
				// Fire event before nether portal removal
				if (!XplatAbstractions.INSTANCE.kindleLensNetherPortalRemoveEvent(player, pos, stack)) {
					level.removeBlock(pos, false);
				}
			}
			if (stateAtOffset.is(Blocks.NETHER_PORTAL)) {
				// Fire event before nether portal removal
				if (!XplatAbstractions.INSTANCE.kindleLensNetherPortalRemoveEvent(player, offPos, stack)) {
					level.removeBlock(offPos, false);
				}
			} else if (BaseFireBlock.canBePlacedAt(level, offPos, dir.getOpposite())) {
				// Fire event before fire placement
				if (!XplatAbstractions.INSTANCE.kindleLensFirePlaceEvent(player, offPos, stack)) {
					level.setBlockAndUpdate(offPos, BaseFireBlock.getState(level, offPos));
				}
			}
		}

		return shouldKill;
	}

}
