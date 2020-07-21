/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.mixin.AccessorAvoidEntityGoal;
import vazkii.botania.mixin.AccessorCreeperEntity;
import vazkii.botania.mixin.AccessorGoalSelector;
import vazkii.botania.mixin.AccessorNearestAttackableTarget;

import java.util.ArrayList;
import java.util.Set;

public class SubTileTigerseye extends TileEntityFunctionalFlower {
	private static final int RANGE = 10;
	private static final int RANGE_Y = 4;
	private static final int COST = 70;
	private static final int SUCCESS_EVENT = 0;

	public SubTileTigerseye() {
		super(ModSubtiles.TIGERSEYE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getWorld().isRemote) {
			return;
		}

		for (CreeperEntity entity : getWorld().getEntitiesWithinAABB(CreeperEntity.class, new AxisAlignedBB(getEffectivePos().add(-RANGE, -RANGE_Y, -RANGE), getEffectivePos().add(RANGE + 1, RANGE_Y + 1, RANGE + 1)))) {
			((AccessorCreeperEntity) entity).setTimeSinceIgnited(2);
			entity.setAttackTarget(null);

			if (getMana() >= COST) {
				boolean did = false;

				Set<PrioritizedGoal> goals = ((AccessorGoalSelector) entity.goalSelector).getGoals();
				boolean hasRunAwayFromPlayerGoal = goals.stream()
						.anyMatch(g -> g.getGoal() instanceof AvoidEntityGoal && ((AccessorAvoidEntityGoal) g.getGoal()).getClassToAvoid() == PlayerEntity.class);
				if (!hasRunAwayFromPlayerGoal) {
					entity.goalSelector.addGoal(3, new AvoidEntityGoal<>(entity, PlayerEntity.class, 6, 1, 1.2));
					did = true;
				}

				for (PrioritizedGoal pg : new ArrayList<>(((AccessorGoalSelector) entity.targetSelector).getGoals())) {
					if (pg.getGoal() instanceof NearestAttackableTargetGoal
							&& ((AccessorNearestAttackableTarget) pg.getGoal()).getTargetClass() == PlayerEntity.class) {
						entity.targetSelector.removeGoal(pg.getGoal());
						did = true;
					}
				}

				if (did) {
					entity.playSound(SoundEvents.ENTITY_CREEPER_HURT, 1.0F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);
					world.addBlockEvent(getPos(), getBlockState().getBlock(), SUCCESS_EVENT, entity.getEntityId());
					addMana(-COST);
					sync();
				}
			}
		}
	}

	@Override
	public boolean receiveClientEvent(int id, int payload) {
		if (id == SUCCESS_EVENT) {
			if (world.isRemote) {
				Entity e = world.getEntityByID(payload);
				if (e != null) {
					float r = (getColor() >> 16 & 0xFF) / 255F;
					float g = (getColor() >> 8 & 0xFF) / 255F;
					float b = (getColor() & 0xFF) / 255F;
					SparkleParticleData data = SparkleParticleData.sparkle(world.rand.nextFloat(), r, g, b, 10);

					for (int i = 0; i < 50; i++) {
						double x = e.getPosX() + world.rand.nextDouble() - 0.5;
						double y = e.getPosY() + e.getHeight() * world.rand.nextDouble();
						double z = e.getPosZ() + world.rand.nextDouble() - 0.5;
						world.addParticle(data, x, y, z, 0, 0, 0);
					}
				}
			}
			return true;
		}
		return super.receiveClientEvent(id, payload);
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), RANGE);
	}

	@Override
	public int getColor() {
		return 0xB1A618;
	}

	@Override
	public int getMaxMana() {
		return 1000;
	}

}
