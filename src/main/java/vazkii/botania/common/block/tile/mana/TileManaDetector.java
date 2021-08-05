/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.mana;

import com.google.common.base.Predicates;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.TileMod;

// TODO(williewillus) apply patch to make detector not-a-BE
public class TileManaDetector extends TileMod implements TickableBlockEntity {
	public TileManaDetector(BlockPos pos, BlockState state) {
		super(ModTiles.MANA_DETECTOR, pos, state);
	}

	@Override
	public void tick() {
		boolean state = getBlockState().getValue(BlockStateProperties.POWERED);
		boolean expectedState = level.getEntitiesOfClass(ThrowableProjectile.class, new AABB(worldPosition, worldPosition.offset(1, 1, 1)), Predicates.instanceOf(IManaBurst.class)).size() != 0;
		if (state != expectedState && !level.isClientSide) {
			level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(BlockStateProperties.POWERED, expectedState));
		}

		if (expectedState) {
			for (int i = 0; i < 4; i++) {
				SparkleParticleData data = SparkleParticleData.sparkle(0.7F + 0.5F * (float) Math.random(), 1F, 0.2F, 0.2F, 5);
				level.addParticle(data, worldPosition.getX() + Math.random(), worldPosition.getY() + Math.random(), worldPosition.getZ() + Math.random(), 0, 0, 0);
			}
		}
	}

}
