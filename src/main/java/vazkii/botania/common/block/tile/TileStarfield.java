/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

public class TileStarfield extends TileMod implements ITickableTileEntity {
	@ObjectHolder(LibMisc.MOD_ID + ":" + LibBlockNames.STARFIELD) public static TileEntityType<TileStarfield> TYPE;

	public TileStarfield() {
		super(TYPE);
	}

	@Override
	public void tick() {
		if (world.isRemote) {
			world.calculateInitialSkylight(); // ensure isDayTime works properly by updating skylightSubtracted
			if (world.isDaytime()) {
				return;
			}

			double radius = 512;
			int iter = 2;
			for (int i = 0; i < iter; i++) {
				double x = pos.getX() + 0.5 + (Math.random() - 0.5) * radius;
				double y = Math.min(256, pos.getY() + Botania.proxy.getClientRenderDistance() * 16);
				double z = pos.getZ() + 0.5 + (Math.random() - 0.5) * radius;

				float w = 0.6F;
				float c = 1F - w;

				float r = w + (float) Math.random() * c;
				float g = w + (float) Math.random() * c;
				float b = w + (float) Math.random() * c;

				float s = 20F + (float) Math.random() * 20F;
				int m = 50;

				SparkleParticleData data = SparkleParticleData.sparkle(s, r, g, b, m);
				Botania.proxy.addParticleForce(world, data, x, y, z, 0, 0, 0);
			}
		}
	}

}
