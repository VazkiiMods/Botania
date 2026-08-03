/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity.flower.functional;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.entity.BotaniaEntities;
import vazkii.botania.common.entity.EnderAirCloudEntity;
import vazkii.botania.common.helper.MathHelper;
import vazkii.botania.common.internal_caps.EnderEssenceCaptured;

import java.util.*;

public class VinculotusBlockEntity extends FunctionalFlowerBlockEntity {
	public static final Set<VinculotusBlockEntity> existingFlowers = Collections.newSetFromMap(new WeakHashMap<>());
	private static final int RANGE = 64;
	private static final int TARGET_RANGE = 1;

	public VinculotusBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.VINCULOTUS, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (!getLevel().isClientSide()) {
			existingFlowers.add(this);
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Circle(getEffectivePos(), RANGE);
	}

	@Override
	public RadiusDescriptor getSecondaryRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), TARGET_RANGE);
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
		if (EnderEssenceCaptured.HOLDER.getOrDefault(entity, false)) {
			// this Enderman won't teleport anymore anyway
			return null;
		}

		int cost = 50;

		List<VinculotusBlockEntity> possibleFlowers = new ArrayList<>();
		for (VinculotusBlockEntity flower : existingFlowers) {
			BlockPos activePos = flower.getEffectivePos();

			if (flower.isPowered() || flower.getMana() <= cost
					|| flower.getLevel() != entity.level()
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
			VinculotusBlockEntity flower = possibleFlowers.get(entity.level().getRandom().nextInt(possibleFlowers.size()));
			BlockPos activePos = flower.getEffectivePos();

			double x = activePos.getX() + 0.5;
			double y = activePos.getY() + 1.5;
			double z = activePos.getZ() + 0.5;

			flower.addMana(-cost);

			// mark it for allowing this teleportation attempt and capturing right after that
			EnderEssenceCaptured.HOLDER.setFor(entity, false);
			// Endermen are 0.6 blocks wide, so +/- 1.2 blocks offset should always fit into the 3x3 block target area
			return new Vec3(x + (Math.random() * 2.4 - 1.2), y, z + (Math.random() * 2.4 - 1.2));
		}

		// remove any potential marker that may have been left over from a previous attempt to capture this EnderMan
		EnderEssenceCaptured.HOLDER.removeFrom(entity);
		return null;
	}

	/**
	 * If this is called right after {@link #onEndermanTeleport(EnderMan, double, double, double)} redirected the
	 * teleport target, the Enderman successfully teleported to the capturing flower.
	 *
	 * @param entity The {@link EnderMan}.
	 */
	public static void postEndermanTeleport(LivingEntity entity) {
		if (EnderEssenceCaptured.HOLDER.existsFor(entity)) {
			EnderAirCloudEntity cloud = BotaniaEntities.ENDER_AIR_CLOUD.create(entity.level());
			if (cloud != null) {
				cloud.moveTo(entity.position(), entity.getYRot(), 0);
				entity.level().addFreshEntity(cloud);
			}

			// completely disable future teleport attempts
			EnderEssenceCaptured.HOLDER.setFor(entity, true);
		}
	}

	/**
	 * Prevent teleportation if the Enderman was stripped of its ability to do so.
	 *
	 * @param entity The {@link EnderMan}.
	 * @return <code>true</code> if the Enderman should not be able to teleport anymore.
	 */
	public static boolean preventEndermanTeleport(LivingEntity entity) {
		return EnderEssenceCaptured.HOLDER.getOrDefault(entity, false);
	}

}
