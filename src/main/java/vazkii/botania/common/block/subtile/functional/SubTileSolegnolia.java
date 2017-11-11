/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 11, 2015, 4:53:35 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import com.google.common.collect.MapMaker;
import net.minecraft.entity.Entity;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.lexicon.LexiconData;

import java.util.Collections;
import java.util.Set;

public class SubTileSolegnolia extends SubTileFunctional {

	private static final double RANGE = 5;
	private static final double RANGE_MINI = 1;

	private static final Set<SubTileSolegnolia> existingFlowers = Collections.newSetFromMap(new MapMaker().concurrencyLevel(2).weakKeys().makeMap());

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(!existingFlowers.contains(this)) {
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
				.filter(f -> f.supertile.getWorld() == e.world)
				.filter(f -> f.supertile.getWorld().getTileEntity(f.supertile.getPos()) == f.supertile)
				.filter(f -> f.supertile.getDistanceSq(e.posX, e.posY, e.posZ) <= f.getRange() * f.getRange())
				.findAny().isPresent();
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
		return new RadiusDescriptor.Circle(toBlockPos(), getRange());
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.solegnolia;
	}

	public static class Mini extends SubTileSolegnolia {
		@Override public double getRange() { return RANGE_MINI; }
	}

}
