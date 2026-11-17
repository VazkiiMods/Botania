/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.proxy.Proxy;

public class StarfieldCreatorBlockEntity extends BlockEntity {
	public StarfieldCreatorBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.STARFIELD_CREATOR, pos, state);
	}

	public static void clientTick(Level level, BlockPos pos, BlockState state, StarfieldCreatorBlockEntity self) {
		level.updateSkyBrightness(); // this isn't called often on clients, but we need so that isDay is accurate.
		if (level.isDay()) {
			return;
		}

		double radius = 512;
		int renderDistance = Proxy.INSTANCE.getClientRenderDistance() * 16;
		int worldHeight = level.getMaxBuildHeight();
		var random = level.getRandom();
		int iter = 2;
		for (int i = 0; i < iter; i++) {
			double x = 0.5 + pos.getX() + (random.nextDouble() - 0.5) * radius;
			double y = 0.5 + Math.min(worldHeight, pos.getY() + renderDistance);
			double z = 0.5 + pos.getZ() + (random.nextDouble() - 0.5) * radius;

			float white = 0.6f;
			float color = 1 - white;

			float red = white + random.nextFloat() * color;
			float green = white + random.nextFloat() * color;
			float blue = white + random.nextFloat() * color;

			float size = 20 + random.nextFloat() * 20;
			int lifeMult = 50;

			SparkleParticleData data = SparkleParticleData.sparkle(size, red, green, blue, lifeMult);
			level.addParticle(data, true, x, y, z, 0, 0, 0);
		}
	}

}
