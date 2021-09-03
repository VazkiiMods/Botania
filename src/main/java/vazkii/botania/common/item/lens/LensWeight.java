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
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.common.core.handler.ConfigHandler;

public class LensWeight extends Lens {
	@Override
	public boolean collideBurst(IManaBurst burst, HitResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		Entity entity = burst.entity();
		if (!entity.level.isClientSide && !burst.isFake() && pos.getType() == HitResult.Type.BLOCK) {
			int harvestLevel = ConfigHandler.COMMON.harvestLevelWeight.getValue();

			BlockPos bPos = ((BlockHitResult) pos).getBlockPos();
			BlockState state = entity.level.getBlockState(bPos);

			if (FallingBlock.isFree(entity.level.getBlockState(bPos.below()))
					&& state.getDestroySpeed(entity.level, bPos) != -1
					&& LensMine.canHarvest(harvestLevel, state)
					&& entity.level.getBlockEntity(bPos) == null) {
				FallingBlockEntity falling = new FallingBlockEntity(entity.level, bPos.getX() + 0.5, bPos.getY(), bPos.getZ() + 0.5, state);
				falling.time = 1;
				entity.level.removeBlock(bPos, false);
				((ServerLevel) entity.level).sendParticles(new BlockParticleOption(ParticleTypes.FALLING_DUST, state), bPos.getX() + 0.5, bPos.getY() + 0.5, bPos.getZ() + 0.5, 10, 0.45, 0.45, 0.45, 5);
				entity.level.addFreshEntity(falling);
			}
		}

		return dead;
	}

}
