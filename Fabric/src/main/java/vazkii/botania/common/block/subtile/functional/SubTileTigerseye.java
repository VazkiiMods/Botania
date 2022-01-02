/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.components.EntityComponents;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.mixin.*;

import java.util.Set;

public class SubTileTigerseye extends TileEntityFunctionalFlower {
	private static final int RANGE = 10;
	private static final int RANGE_Y = 4;
	private static final int COST = 70;
	private static final int SUCCESS_EVENT = 0;

	public SubTileTigerseye(BlockPos pos, BlockState state) {
		super(ModSubtiles.TIGERSEYE, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getLevel().isClientSide) {
			return;
		}

		for (Creeper entity : getLevel().getEntitiesOfClass(Creeper.class, new AABB(getEffectivePos().offset(-RANGE, -RANGE_Y, -RANGE), getEffectivePos().offset(RANGE + 1, RANGE_Y + 1, RANGE + 1)))) {
			((AccessorCreeper) entity).setCurrentFuseTime(2);
			entity.setTarget(null);

			if (getMana() >= COST) {
				if (pacifyCreeper(entity)) {
					EntityComponents.TIGERSEYE.get(entity).setPacified();
					entity.playSound(ModSounds.tigerseyePacify, 1.0F, (level.random.nextFloat() - level.random.nextFloat()) * 0.2F + 1.0F);
					level.blockEvent(getBlockPos(), getBlockState().getBlock(), SUCCESS_EVENT, entity.getId());
					addMana(-COST);
					sync();
				}
			}
		}
	}

	public static void pacifyAfterLoad(Entity entity, ServerLevel level) {
		if (entity instanceof Creeper creeper && EntityComponents.TIGERSEYE.get(creeper).isPacified()) {
			pacifyCreeper(creeper);
		}
	}

	private static boolean pacifyCreeper(Creeper creeper) {
		boolean did = false;
		GoalSelector goalSelector = ((AccessorMob) creeper).getGoalSelector();
		Set<WrappedGoal> goals = ((AccessorGoalSelector) goalSelector).getAvailableGoals();
		for (var goal : goals) {
			Goal wrapped = goal.getGoal();
			if (wrapped instanceof CreeperAvoidPlayerGoal playerGoal && !playerGoal.enabled) {
				playerGoal.enable();
				did = true;
				break;
			}
		}

		GoalSelector targetSelector = ((AccessorMob) creeper).getTargetSelector();
		for (var iterator = ((AccessorGoalSelector) targetSelector).getAvailableGoals().iterator(); iterator.hasNext();) {
			WrappedGoal pg = iterator.next();
			if (pg.getGoal() instanceof AccessorNearestAttackableTargetGoal targetGoal
					&& targetGoal.getTargetClass() == Player.class) {
				iterator.remove();
				did = true;
			}
		}
		return did;
	}

	@Override
	public boolean triggerEvent(int id, int payload) {
		if (id == SUCCESS_EVENT) {
			if (level.isClientSide) {
				Entity e = level.getEntity(payload);
				if (e != null) {
					float r = (getColor() >> 16 & 0xFF) / 255F;
					float g = (getColor() >> 8 & 0xFF) / 255F;
					float b = (getColor() & 0xFF) / 255F;
					SparkleParticleData data = SparkleParticleData.sparkle(level.random.nextFloat(), r, g, b, 10);

					for (int i = 0; i < 50; i++) {
						double x = e.getX() + level.random.nextDouble() - 0.5;
						double y = e.getY() + e.getBbHeight() * level.random.nextDouble();
						double z = e.getZ() + level.random.nextDouble() - 0.5;
						level.addParticle(data, x, y, z, 0, 0, 0);
					}
				}
			}
			return true;
		}
		return super.triggerEvent(id, payload);
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

	public static class CreeperAvoidPlayerGoal extends AvoidEntityGoal<Player> {
		private boolean enabled = false;

		public CreeperAvoidPlayerGoal(Creeper mob) {
			this(mob, 6.0F, 1.0, 1.2);
		}

		private CreeperAvoidPlayerGoal(Creeper mob, float maxDist, double walkSpeedModifier, double sprintSpeedModifier) {
			super(mob, Player.class, maxDist, walkSpeedModifier, sprintSpeedModifier);
		}

		public void enable() {
			enabled = true;
		}

		@Override
		public boolean canUse() {
			return enabled && super.canUse();
		}
	}

}
