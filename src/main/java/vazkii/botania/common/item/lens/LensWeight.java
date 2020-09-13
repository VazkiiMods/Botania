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
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.server.ServerWorld;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.common.core.handler.ConfigHandler;

public class LensWeight extends Lens {
	@Override
	public boolean collideBurst(IManaBurst burst, RayTraceResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		Entity entity = burst.entity();
		if (!entity.world.isRemote && !burst.isFake() && pos.getType() == RayTraceResult.Type.BLOCK) {
			int harvestLevel = ConfigHandler.COMMON.harvestLevelWeight.get();

			BlockPos bPos = ((BlockRayTraceResult) pos).getPos();
			Block block = entity.world.getBlockState(bPos).getBlock();
			BlockState state = entity.world.getBlockState(bPos);
			int neededHarvestLevel = block.getHarvestLevel(state);

			if (FallingBlock.canFallThrough(entity.world.getBlockState(bPos.down()))
					&& state.getBlockHardness(entity.world, bPos) != -1
					&& neededHarvestLevel <= harvestLevel
					&& entity.world.getTileEntity(bPos) == null) {
				FallingBlockEntity falling = new FallingBlockEntity(entity.world, bPos.getX() + 0.5, bPos.getY(), bPos.getZ() + 0.5, state);
				falling.fallTime = 1;
				entity.world.removeBlock(bPos, false);
				((ServerWorld) entity.world).spawnParticle(new BlockParticleData(ParticleTypes.FALLING_DUST, state), bPos.getX() + 0.5, bPos.getY() + 0.5, bPos.getZ() + 0.5, 10, 0.45, 0.45, 0.45, 5);
				entity.world.addEntity(falling);
			}
		}

		return dead;
	}

}
