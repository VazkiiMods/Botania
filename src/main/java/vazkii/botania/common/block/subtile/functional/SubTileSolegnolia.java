/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import com.google.common.collect.MapMaker;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntityType;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;

import java.util.Collections;
import java.util.Set;

public class SubTileSolegnolia extends TileEntityFunctionalFlower {
	private static final double RANGE = 5;
	private static final double RANGE_MINI = 1;

	private static final Set<SubTileSolegnolia> existingFlowers = Collections.newSetFromMap(new MapMaker().concurrencyLevel(2).weakKeys().makeMap());

	public SubTileSolegnolia(TileEntityType<?> type) {
		super(type);
	}

	public SubTileSolegnolia() {
		this(ModSubtiles.SOLEGNOLIA);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (!existingFlowers.contains(this)) {
			existingFlowers.add(this);
		}
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	public static boolean hasSolegnoliaAround(Entity e) {
		return existingFlowers.stream()
				.filter(f -> f.redstoneSignal == 0)
				.filter(f -> f.getWorld() == e.world)
				.anyMatch(f -> f.getEffectivePos().distanceSq(e.getPosX(), e.getPosY(), e.getPosZ(), true) <= f.getRange() * f.getRange());
	}

	@Override
	public int getMaxMana() {
		return 1;
	}

	@Override
	public int getColor() {
		return 0xC99C4D;
	}

	public double getRange() {
		return RANGE;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Circle(getEffectivePos(), getRange());
	}

	public static class Mini extends SubTileSolegnolia {
		public Mini() {
			super(ModSubtiles.SOLEGNOLIA_CHIBI);
		}

		@Override
		public double getRange() {
			return RANGE_MINI;
		}
	}

}
