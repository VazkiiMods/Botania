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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.internal.IManaBurst;

public class LensFire extends Lens {
	@Override
	public void updateBurst(IManaBurst burst, ItemStack stack) {
		Entity entity = burst.entity();
		if (!entity.level.isClientSide) {
			entity.setSecondsOnFire(3);
		}
	}

	@Override
	public boolean collideBurst(IManaBurst burst, HitResult rtr, boolean isManaBlock, boolean shouldKill, ItemStack stack) {
		Entity entity = burst.entity();

		if (!entity.level.isClientSide && rtr.getType() == HitResult.Type.BLOCK
				&& !burst.isFake() && !isManaBlock) {
			BlockHitResult brtr = (BlockHitResult) rtr;
			BlockPos pos = brtr.getBlockPos();
			Direction dir = brtr.getDirection();

			BlockPos offPos = pos.relative(dir);

			BlockState stateAt = entity.level.getBlockState(pos);
			BlockState stateAtOffset = entity.level.getBlockState(offPos);

			if (stateAt.is(Blocks.NETHER_PORTAL)) {
				entity.level.removeBlock(pos, false);
			}
			if (stateAtOffset.is(Blocks.NETHER_PORTAL)) {
				entity.level.removeBlock(offPos, false);
			} else if (stateAtOffset.isAir()) {
				entity.level.setBlockAndUpdate(offPos, Blocks.FIRE.defaultBlockState());
			}
		}

		return shouldKill;
	}

}
