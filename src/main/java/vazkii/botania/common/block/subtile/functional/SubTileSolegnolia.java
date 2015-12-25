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

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

import net.minecraft.entity.Entity;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileSolegnolia extends SubTileFunctional {

	private static final double RANGE = 5;
	private static final double RANGE_MINI = 1;

	public static Set<SubTileSolegnolia> existingFlowers = Collections.newSetFromMap(new WeakHashMap());
	private static boolean registered = false;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(!existingFlowers.contains(this)) {
			existingFlowers.add(this);
			if(!registered)
				registered = true;
		}
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	public static boolean hasSolegnoliaAround(Entity e) {
		for(SubTileSolegnolia flower : existingFlowers) {
			if(flower.redstoneSignal > 0 || flower.supertile.getWorldObj() != e.worldObj || flower.supertile.getWorldObj().getTileEntity(flower.supertile.xCoord, flower.supertile.yCoord, flower.supertile.zCoord) != flower.supertile)
				continue;

			double range = flower.getRange();
			if(MathHelper.pointDistanceSpace(e.posX, e.posY, e.posZ, flower.supertile.xCoord + 0.5, flower.supertile.yCoord + 0.5, flower.supertile.zCoord + 0.5) <= range)
				return true;
		}

		return false;
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
		return new RadiusDescriptor.Circle(toChunkCoordinates(), getRange());
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.solegnolia;
	}

	public static class Mini extends SubTileSolegnolia {
		@Override public double getRange() { return RANGE_MINI; }
	}

}
