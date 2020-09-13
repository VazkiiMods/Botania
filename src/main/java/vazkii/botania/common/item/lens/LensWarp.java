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
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.common.block.BlockPistonRelay;
import vazkii.botania.common.block.ModBlocks;

public class LensWarp extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, RayTraceResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		ThrowableEntity entity = burst.entity();
		if (entity.world.isRemote || burst.isFake() || pos.getType() != RayTraceResult.Type.BLOCK) {
			return dead;
		}

		BlockPos hit = ((BlockRayTraceResult) pos).getPos();
		Block block = entity.world.getBlockState(hit).getBlock();
		if (block == ModBlocks.pistonRelay) {
			BlockPistonRelay.WorldData data = BlockPistonRelay.WorldData.get(entity.world);
			BlockPos dest = data.mapping.get(hit);

			if (dest != null) {
				entity.setPosition(dest.getX() + 0.5, dest.getY() + 0.5, dest.getZ() + 0.5);
				burst.setCollidedAt(dest);

				burst.setWarped(true);

				return false;
			}
		}
		return dead;
	}
}
