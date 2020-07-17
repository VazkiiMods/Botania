/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class SubTileBergamute extends TileEntityFunctionalFlower {
	private static final int RANGE = 4;
	private static final Set<SubTileBergamute> existingFlowers = Collections.newSetFromMap(new WeakHashMap<>());

	public SubTileBergamute() {
		super(ModSubtiles.BERGAMUTE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getWorld().isRemote) {
			if (!existingFlowers.contains(this)) {
				existingFlowers.add(this);
			}
		}
	}

	@Override
	public void remove() {
		super.remove();
		existingFlowers.remove(this);
	}

	// todo seems expensive when we have lots of sounds cache maybe?
	protected static SubTileBergamute getBergamuteNearby(double x, double y, double z) {
		return existingFlowers.stream()
				.filter(f -> f.redstoneSignal == 0)
				.filter(f -> f.getEffectivePos().distanceSq(x, y, z, false) <= RANGE * RANGE)
				.findAny().orElse(null);
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@Override
	public int getMaxMana() {
		return 1;
	}

	@Override
	public int getColor() {
		return 0xF46C6C;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Circle(getEffectivePos(), RANGE);
	}

}
