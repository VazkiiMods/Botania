/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.helper.MathHelper;

import javax.annotation.Nullable;

import java.util.*;

public class SubTileVinculotus extends TileEntityFunctionalFlower {
	public static final Set<SubTileVinculotus> existingFlowers = Collections.newSetFromMap(new WeakHashMap<>());
	private static final int RANGE = 64;

	public SubTileVinculotus() {
		super(ModSubtiles.VINCULOTUS);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (!getWorld().isClient) {
			existingFlowers.add(this);
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Circle(getEffectivePos(), RANGE);
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@Override
	public int getColor() {
		return 0x0A6051;
	}

	@Override
	public int getMaxMana() {
		return 500;
	}

	@Nullable
	public static Vec3d onEndermanTeleport(EndermanEntity entity, double targetX, double targetY, double targetZ) {
		int cost = 50;

		List<SubTileVinculotus> possibleFlowers = new ArrayList<>();
		for (SubTileVinculotus flower : existingFlowers) {
			BlockPos activePos = flower.getEffectivePos();

			if (flower.redstoneSignal > 0 || flower.getMana() <= cost
					|| flower.getWorld() != entity.world
					|| flower.getWorld().getBlockEntity(flower.getPos()) != flower) {
				continue;
			}

			double x = activePos.getX() + 0.5;
			double y = activePos.getY() + 1.5;
			double z = activePos.getZ() + 0.5;

			if (MathHelper.pointDistanceSpace(x, y, z, targetX, targetY, targetZ) < RANGE) {
				possibleFlowers.add(flower);
			}
		}

		if (!possibleFlowers.isEmpty()) {
			SubTileVinculotus flower = possibleFlowers.get(entity.world.random.nextInt(possibleFlowers.size()));
			BlockPos activePos = flower.getEffectivePos();

			double x = activePos.getX() + 0.5;
			double y = activePos.getY() + 1.5;
			double z = activePos.getZ() + 0.5;

			flower.addMana(-cost);
			flower.sync();

			return new Vec3d(x + Math.random() * 3 - 1, y, z + Math.random() * 3 - 1);
		}

		return null;
	}
}
