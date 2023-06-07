/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.flower.functional;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.common.block.BotaniaFlowerBlocks;
import vazkii.botania.common.helper.MathHelper;

import java.util.*;

public class VinculotusBlockEntity extends FunctionalFlowerBlockEntity {
	public static final Set<VinculotusBlockEntity> existingFlowers = Collections.newSetFromMap(new WeakHashMap<>());
	private static final int RANGE = 64;

	public VinculotusBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaFlowerBlocks.VINCULOTUS, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (!getLevel().isClientSide) {
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
	public static Vec3 onEndermanTeleport(EnderMan entity, double targetX, double targetY, double targetZ) {
		int cost = 50;

		List<VinculotusBlockEntity> possibleFlowers = new ArrayList<>();
		for (VinculotusBlockEntity flower : existingFlowers) {
			BlockPos activePos = flower.getEffectivePos();

			if (flower.redstoneSignal > 0 || flower.getMana() <= cost
					|| flower.getLevel() != entity.getLevel()
					|| flower.getLevel().getBlockEntity(flower.getBlockPos()) != flower) {
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
			VinculotusBlockEntity flower = possibleFlowers.get(entity.getLevel().random.nextInt(possibleFlowers.size()));
			BlockPos activePos = flower.getEffectivePos();

			double x = activePos.getX() + 0.5;
			double y = activePos.getY() + 1.5;
			double z = activePos.getZ() + 0.5;

			flower.addMana(-cost);
			flower.sync();

			return new Vec3(x + Math.random() * 3 - 1, y, z + Math.random() * 3 - 1);
		}

		return null;
	}
}
