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
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.common.core.handler.ConfigHandler;

public class LensWeight extends Lens {
	@Override
	public boolean collideBurst(IManaBurst burst, ThrownEntity entity, HitResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		if (!entity.world.isClient && !burst.isFake() && pos.getType() == HitResult.Type.BLOCK) {
			int harvestLevel = ConfigHandler.COMMON.harvestLevelWeight.getValue();

			BlockPos bPos = ((BlockHitResult) pos).getBlockPos();
			Block block = entity.world.getBlockState(bPos).getBlock();
			BlockState state = entity.world.getBlockState(bPos);
			int neededHarvestLevel = -1 /* todo 1.16-fabric block.getHarvestLevel(state) */;

			if (entity.world.isAir(bPos.down())
					&& state.getHardness(entity.world, bPos) != -1
					&& neededHarvestLevel <= harvestLevel
					&& entity.world.getBlockEntity(bPos) == null) {
				FallingBlockEntity falling = new FallingBlockEntity(entity.world, bPos.getX() + 0.5, bPos.getY(), bPos.getZ() + 0.5, state);
				falling.timeFalling = 1;
				entity.world.removeBlock(bPos, false);
				((ServerWorld) entity.world).spawnParticles(new BlockStateParticleEffect(ParticleTypes.FALLING_DUST, state), bPos.getX() + 0.5, bPos.getY() + 0.5, bPos.getZ() + 0.5, 10, 0.45, 0.45, 0.45, 5);
				entity.world.spawnEntity(falling);
			}
		}

		return dead;
	}

}
