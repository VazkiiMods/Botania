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

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;

import java.util.Collections;
import java.util.Set;

public class SubTileSolegnolia extends TileEntityFunctionalFlower {
	private static final double RANGE = 5;
	private static final double RANGE_MINI = 1;

	private static final Set<SubTileSolegnolia> existingFlowers = Collections.newSetFromMap(new MapMaker().concurrencyLevel(2).weakKeys().makeMap());

	protected SubTileSolegnolia(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public SubTileSolegnolia(BlockPos pos, BlockState state) {
		this(ModSubtiles.SOLEGNOLIA, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (!existingFlowers.contains(this)) {
			existingFlowers.add(this);
		}
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		existingFlowers.remove(this);
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	public static boolean hasSolegnoliaAround(Entity e) {
		return existingFlowers.stream()
				.filter(f -> f.redstoneSignal == 0)
				.filter(f -> f.getLevel() == e.level)
				.anyMatch(f -> f.getEffectivePos().distSqr(e.getX(), e.getY(), e.getZ(), true) <= f.getRange() * f.getRange());
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
		public Mini(BlockPos pos, BlockState state) {
			super(ModSubtiles.SOLEGNOLIA_CHIBI, pos, state);
		}

		@Override
		public double getRange() {
			return RANGE_MINI;
		}
	}

}
