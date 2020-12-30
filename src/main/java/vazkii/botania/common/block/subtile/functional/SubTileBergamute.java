/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.util.math.Direction;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.block.tile.mana.TilePool;

import javax.annotation.Nullable;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class SubTileBergamute extends TileEntitySpecialFlower {
	private static final int RANGE = 4;
	private static final Set<SubTileBergamute> existingFlowers = Collections.newSetFromMap(new WeakHashMap<>());
	private static final Set<SoundInstance> mutedSounds = Collections.newSetFromMap(new WeakHashMap<>());
	private boolean disabled = false;

	public SubTileBergamute() {
		super(ModSubtiles.BERGAMUTE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getWorld().isClient) {
			disabled = false;
			for (Direction dir : Direction.values()) {
				int redstoneSide = getWorld().getEmittedRedstonePower(getPos().offset(dir), dir);
				if (redstoneSide > 0) {
					disabled = true;
					break;
				}
			}
			existingFlowers.add(this);
		}
	}

	@Override
	public void markRemoved() {
		super.markRemoved();
		existingFlowers.remove(this);
	}

	// todo seems expensive when we have lots of sounds cache maybe?
	@Nullable
	private static SubTileBergamute getBergamuteNearby(double x, double y, double z) {
		for (SubTileBergamute f : existingFlowers) {
			if (!f.disabled && f.getEffectivePos().getSquaredDistance(x, y, z, true) <= RANGE * RANGE) {
				return f;
			}
		}
		return null;
	}

	@Environment(EnvType.CLIENT)
	public static boolean muteSound(SoundInstance sound) {
		SubTileBergamute berg = getBergamuteNearby(sound.getX(), sound.getY(), sound.getZ());
		if (berg != null) {
			if (mutedSounds.add(sound) && Math.random() < 0.5) {
				int color = TilePool.PARTICLE_COLOR;
				float red = (color >> 16 & 0xFF) / 255F;
				float green = (color >> 8 & 0xFF) / 255F;
				float blue = (color & 0xFF) / 255F;
				SparkleParticleData data = SparkleParticleData.sparkle((float) Math.random(), red, green, blue, 5);
				berg.getWorld().addParticle(data, berg.getEffectivePos().getX() + 0.3 + Math.random() * 0.5, berg.getEffectivePos().getY() + 0.5 + Math.random() * 0.5, berg.getEffectivePos().getZ() + 0.3 + Math.random() * 0.5, 0, 0, 0);
			}
		}
		return berg != null;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Circle(getEffectivePos(), RANGE);
	}

}
