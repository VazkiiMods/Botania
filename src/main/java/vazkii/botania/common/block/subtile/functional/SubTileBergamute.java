/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import com.mojang.datafixers.util.Pair;

import net.minecraft.client.audio.ISound;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.block.tile.mana.TilePool;

import java.util.*;

public class SubTileBergamute extends TileEntitySpecialFlower {
	private static final int RANGE = 4;
	private static final Set<SubTileBergamute> existingFlowers = Collections.newSetFromMap(new WeakHashMap<>());
	private static final Set<ISound> mutedSounds = Collections.newSetFromMap(new WeakHashMap<>());
	private boolean disabled = false;

	public SubTileBergamute() {
		super(ModSubtiles.BERGAMUTE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getWorld().isRemote) {
			disabled = getWorld().isBlockPowered(getPos());
			existingFlowers.add(this);
		}
	}

	@Override
	public void remove() {
		super.remove();
		existingFlowers.remove(this);
	}

	// todo seems expensive when we have lots of sounds cache maybe?
	private static Pair<Integer, SubTileBergamute> getBergamutesNearby(double x, double y, double z, int maxCount) {
		int count = 0;
		SubTileBergamute tile = null;

		for (SubTileBergamute f : existingFlowers) {
			if (!f.disabled && f.getEffectivePos().distanceSq(x, y, z, true) <= RANGE * RANGE) {
				count++;
				if (count == 1) {
					tile = f;
				}
				if (count >= maxCount) {
					break;
				}
			}
		}
		return Pair.of(count, tile);
	}

	public static boolean isBergamuteNearby(double x, double y, double z) {
		return getBergamutesNearby(x, y, z, 1).getFirst() > 0;
	}

	@OnlyIn(Dist.CLIENT)
	public static int countFlowersAround(ISound sound) {
		// We halve the volume for each flower (see MixinSoundEngine)
		// halving 8 times already brings the multiplier to near zero, so no
		// need to keep going if we've seen more than 8.
		Pair<Integer, SubTileBergamute> countAndBerg = getBergamutesNearby(sound.getX(), sound.getY(), sound.getZ(), 8);
		int count = countAndBerg.getFirst();
		if (count > 0) {
			if (mutedSounds.add(sound) && Math.random() < 0.5) {
				int color = TilePool.PARTICLE_COLOR;
				float red = (color >> 16 & 0xFF) / 255F;
				float green = (color >> 8 & 0xFF) / 255F;
				float blue = (color & 0xFF) / 255F;
				SparkleParticleData data = SparkleParticleData.sparkle((float) Math.random(), red, green, blue, 5);
				SubTileBergamute berg = countAndBerg.getSecond();
				berg.getWorld().addParticle(data, berg.getEffectivePos().getX() + 0.3 + Math.random() * 0.5, berg.getEffectivePos().getY() + 0.5 + Math.random() * 0.5, berg.getEffectivePos().getZ() + 0.3 + Math.random() * 0.5, 0, 0, 0);
			}
		}
		return count;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Circle(getEffectivePos(), RANGE);
	}

}
