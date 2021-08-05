/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.Botania;

public class TileStarfield extends TileMod implements TickableBlockEntity {
	public TileStarfield(BlockPos pos, BlockState state) {
		super(ModTiles.STARFIELD, pos, state);
	}

	@Override
	public void tick() {
		if (level.isClientSide) {
			level.updateSkyBrightness(); // ensure isDayTime works properly by updating skylightSubtracted
			if (level.isDay()) {
				return;
			}

			double radius = 512;
			int iter = 2;
			for (int i = 0; i < iter; i++) {
				double x = worldPosition.getX() + 0.5 + (Math.random() - 0.5) * radius;
				double y = Math.min(256, worldPosition.getY() + Botania.proxy.getClientRenderDistance() * 16);
				double z = worldPosition.getZ() + 0.5 + (Math.random() - 0.5) * radius;

				float w = 0.6F;
				float c = 1F - w;

				float r = w + (float) Math.random() * c;
				float g = w + (float) Math.random() * c;
				float b = w + (float) Math.random() * c;

				float s = 20F + (float) Math.random() * 20F;
				int m = 50;

				SparkleParticleData data = SparkleParticleData.sparkle(s, r, g, b, m);
				Botania.proxy.addParticleForce(level, data, x, y, z, 0, 0, 0);
			}
		}
	}

}
