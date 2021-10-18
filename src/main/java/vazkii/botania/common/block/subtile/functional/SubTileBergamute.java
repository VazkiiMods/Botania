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

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.block.tile.mana.TilePool;

import java.util.*;

public class SubTileBergamute extends TileEntitySpecialFlower {
	private static final int RANGE = 4;
	private static final Set<SubTileBergamute> existingFlowers = Collections.newSetFromMap(new WeakHashMap<>());
	private static final Set<SoundInstance> mutedSounds = Collections.newSetFromMap(new WeakHashMap<>());
	private boolean disabled = false;

	public SubTileBergamute(BlockPos pos, BlockState state) {
		super(ModSubtiles.BERGAMUTE, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getLevel().isClientSide) {
			disabled = getLevel().hasNeighborSignal(getBlockPos());
			existingFlowers.add(this);
		}
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		existingFlowers.remove(this);
	}

	private static Pair<Integer, SubTileBergamute> getBergamutesNearby(double x, double y, double z) {
		int count = 0;
		SubTileBergamute tile = null;

		for (SubTileBergamute f : existingFlowers) {
			if (!f.disabled && f.getEffectivePos().distSqr(x, y, z, true) <= RANGE * RANGE) {
				count++;
				if (count == 1) {
					tile = f;
				} else if (count >= 8) {
					// We halve the volume for each flower (see MixinSoundEngine)
					// halving 8 times already brings the multiplier to near zero, so no
					// need to keep going if we've seen more than 8.
					break;
				}
			}
		}
		return Pair.of(count, tile);
	}

	@Environment(EnvType.CLIENT)
	public static int countFlowersAround(SoundInstance sound) {
		Pair<Integer, SubTileBergamute> countAndBerg = getBergamutesNearby(sound.getX(), sound.getY(), sound.getZ());
		int count = countAndBerg.getFirst();
		if (count > 0) {
			if (mutedSounds.add(sound) && Math.random() < 0.5) {
				int color = TilePool.PARTICLE_COLOR;
				float red = (color >> 16 & 0xFF) / 255F;
				float green = (color >> 8 & 0xFF) / 255F;
				float blue = (color & 0xFF) / 255F;
				SparkleParticleData data = SparkleParticleData.sparkle((float) Math.random(), red, green, blue, 5);
				SubTileBergamute berg = countAndBerg.getSecond();
				berg.getLevel().addParticle(data, berg.getEffectivePos().getX() + 0.3 + Math.random() * 0.5, berg.getEffectivePos().getY() + 0.5 + Math.random() * 0.5, berg.getEffectivePos().getZ() + 0.3 + Math.random() * 0.5, 0, 0, 0);
			}
		}
		return count;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Circle(getEffectivePos(), RANGE);
	}

}
