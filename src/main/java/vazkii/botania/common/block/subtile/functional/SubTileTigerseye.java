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
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.mixin.*;

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

		if (getWorld().isClient) {
			return;
		}

		for (CreeperEntity entity : getWorld().getNonSpectatingEntities(CreeperEntity.class, new Box(getEffectivePos().add(-RANGE, -RANGE_Y, -RANGE), getEffectivePos().add(RANGE + 1, RANGE_Y + 1, RANGE + 1)))) {
			((AccessorCreeperEntity) entity).setCurrentFuseTime(2);
			entity.setTarget(null);

			if (getMana() >= COST) {
				boolean did = false;

				GoalSelector goalSelector = ((AccessorMobEntity) entity).getGoalSelector();
				Set<PrioritizedGoal> goals = ((AccessorGoalSelector) goalSelector).getGoals();
				boolean hasRunAwayFromPlayerGoal = goals.stream()
						.anyMatch(g -> g.getGoal() instanceof FleeEntityGoal && ((AccessorAvoidEntityGoal) g.getGoal()).getClassToFleeFrom() == PlayerEntity.class);
				if (!hasRunAwayFromPlayerGoal) {
					goalSelector.add(3, new FleeEntityGoal<>(entity, PlayerEntity.class, 6, 1, 1.2));
					did = true;
				}

				GoalSelector targetSelector = ((AccessorMobEntity) entity).getTargetSelector();
				for (PrioritizedGoal pg : new ArrayList<>(((AccessorGoalSelector) targetSelector).getGoals())) {
					if (pg.getGoal() instanceof FollowTargetGoal
							&& ((AccessorNearestAttackableTarget) pg.getGoal()).getTargetClass() == PlayerEntity.class) {
						targetSelector.remove(pg.getGoal());
						did = true;
					}
				}

				if (did) {
					entity.playSound(SoundEvents.ENTITY_CREEPER_HURT, 1.0F, (world.random.nextFloat() - world.random.nextFloat()) * 0.2F + 1.0F);
					world.addSyncedBlockEvent(getPos(), getCachedState().getBlock(), SUCCESS_EVENT, entity.getEntityId());
					addMana(-COST);
					sync();
				}
			}
		}
	}

	@Override
	public boolean onSyncedBlockEvent(int id, int payload) {
		if (id == SUCCESS_EVENT) {
			if (world.isClient) {
				Entity e = world.getEntityById(payload);
				if (e != null) {
					float r = (getColor() >> 16 & 0xFF) / 255F;
					float g = (getColor() >> 8 & 0xFF) / 255F;
					float b = (getColor() & 0xFF) / 255F;
					SparkleParticleData data = SparkleParticleData.sparkle(world.random.nextFloat(), r, g, b, 10);

					for (int i = 0; i < 50; i++) {
						double x = e.getX() + world.random.nextDouble() - 0.5;
						double y = e.getY() + e.getHeight() * world.random.nextDouble();
						double z = e.getZ() + world.random.nextDouble() - 0.5;
						world.addParticle(data, x, y, z, 0, 0, 0);
					}
				}
			}
			return true;
		}
		return super.onSyncedBlockEvent(id, payload);
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
