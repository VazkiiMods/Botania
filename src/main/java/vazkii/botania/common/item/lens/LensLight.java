/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.lens;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileManaFlame;

public class LensLight extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, ThrownEntity entity, HitResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		BlockPos coords = burst.getBurstSourceBlockPos();
		if (!entity.world.isClient && pos.getType() == HitResult.Type.BLOCK && !burst.isFake() && !isManaBlock) {
			BlockHitResult rtr = (BlockHitResult) pos;
			if (!coords.equals(rtr.getBlockPos())) {
				BlockPos neighborPos = rtr.getBlockPos().offset(rtr.getSide());

				Block blockAt = entity.world.getBlockState(rtr.getBlockPos()).getBlock();
				BlockState neighbor = entity.world.getBlockState(neighborPos);

				if (blockAt == ModBlocks.manaFlame) {
					entity.world.setBlockState(rtr.getBlockPos(), Blocks.AIR.getDefaultState());
				} else if (neighbor.isAir() || neighbor.getMaterial().isReplaceable()) {
					entity.world.setBlockState(neighborPos, ModBlocks.manaFlame.getDefaultState());
					BlockEntity tile = entity.world.getBlockEntity(neighborPos);

					if (tile instanceof TileManaFlame) {
						((TileManaFlame) tile).setColor(burst.getColor());
					}
				}
			}
		}

		return dead;
	}

}
