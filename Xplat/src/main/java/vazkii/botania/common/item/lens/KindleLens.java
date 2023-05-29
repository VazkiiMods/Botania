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
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.internal.ManaBurst;

public class KindleLens extends Lens {
	@Override
	public void updateBurst(ManaBurst burst, ItemStack stack) {
		Entity entity = burst.entity();
		if (!entity.getLevel().isClientSide) {
			entity.setSecondsOnFire(3);
		}
	}

	@Override
	public boolean collideBurst(ManaBurst burst, HitResult rtr, boolean isManaBlock, boolean shouldKill, ItemStack stack) {
		Entity entity = burst.entity();

		if (!entity.getLevel().isClientSide && rtr.getType() == HitResult.Type.BLOCK
				&& !burst.isFake() && !isManaBlock) {
			BlockHitResult brtr = (BlockHitResult) rtr;
			BlockPos pos = brtr.getBlockPos();
			Direction dir = brtr.getDirection();

			BlockPos offPos = pos.relative(dir);

			BlockState stateAt = entity.getLevel().getBlockState(pos);
			BlockState stateAtOffset = entity.getLevel().getBlockState(offPos);

			if (stateAt.is(Blocks.NETHER_PORTAL)) {
				entity.getLevel().removeBlock(pos, false);
			}
			if (stateAtOffset.is(Blocks.NETHER_PORTAL)) {
				entity.getLevel().removeBlock(offPos, false);
			} else if (BaseFireBlock.canBePlacedAt(entity.getLevel(), offPos, dir.getOpposite())) {
				entity.getLevel().setBlockAndUpdate(offPos, BaseFireBlock.getState(entity.getLevel(), offPos));
			}
		}

		return shouldKill;
	}

}
