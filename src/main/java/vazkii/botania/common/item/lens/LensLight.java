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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileManaFlame;

public class LensLight extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, HitResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		Entity entity = burst.entity();
		if (!entity.level.isClientSide && pos.getType() == HitResult.Type.BLOCK && !burst.isFake() && !isManaBlock) {
			BlockHitResult rtr = (BlockHitResult) pos;
			BlockPos neighborPos = rtr.getBlockPos().relative(rtr.getDirection());

			Block blockAt = entity.level.getBlockState(rtr.getBlockPos()).getBlock();
			BlockState neighbor = entity.level.getBlockState(neighborPos);

			if (blockAt == ModBlocks.manaFlame) {
				entity.level.removeBlock(rtr.getBlockPos(), false);
			} else if (neighbor.isAir() || neighbor.getMaterial().isReplaceable()) {
				entity.level.setBlockAndUpdate(neighborPos, ModBlocks.manaFlame.defaultBlockState());
				BlockEntity tile = entity.level.getBlockEntity(neighborPos);

				if (tile instanceof TileManaFlame) {
					((TileManaFlame) tile).setColor(burst.getColor());
				}
			}
		}

		return dead;
	}

}
