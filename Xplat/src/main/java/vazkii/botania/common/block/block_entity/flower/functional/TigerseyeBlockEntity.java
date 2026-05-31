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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.internal_caps.TigerseyePacified;
import vazkii.botania.mixin.*;

import java.util.Set;

public class TigerseyeBlockEntity extends FunctionalFlowerBlockEntity {
	private static final int RANGE = 10;
	private static final int RANGE_Y = 4;
	private static final int COST = 70;
	private static final int SUCCESS_EVENT = 0;

	public TigerseyeBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.TIGERSEYE, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getLevel().isClientSide() || getMana() < COST) {
			return;
		}

		for (Creeper entity : getLevel().getEntitiesOfClass(Creeper.class,
				new AABB(getEffectivePos()).inflate(RANGE, RANGE_Y, RANGE), Creeper::isAlive)) {
			((CreeperAccessor) entity).botania_setSwell(2);
			entity.setTarget(null);

			if (pacifyCreeper(entity)) {
				TigerseyePacified.MARKER.addFor(entity);
				entity.playSound(BotaniaSounds.tigerseyePacify, 1.0F, (float) level.getRandom().triangle(1.0, 0.2));
				level.blockEvent(getBlockPos(), getBlockState().getBlock(), SUCCESS_EVENT, entity.getId());
				addMana(-COST);
				if (getMana() < COST) {
					break;
				}
			}
		}
	}

	public static void pacifyAfterLoad(Entity entity, ServerLevel level) {
		if (entity instanceof Creeper creeper && TigerseyePacified.MARKER.existsFor(creeper)) {
			pacifyCreeper(creeper);
		}
	}

	private static boolean pacifyCreeper(Creeper creeper) {
		boolean did = false;
		GoalSelector goalSelector = ((MobAccessor) creeper).botania_getGoalSelector();
		Set<WrappedGoal> goals = goalSelector.getAvailableGoals();
		for (var goal : goals) {
			Goal wrapped = goal.getGoal();
			if (wrapped instanceof CreeperAvoidPlayerGoal playerGoal && !playerGoal.enabled) {
				playerGoal.enable();
				did = true;
				break;
			}
		}

		GoalSelector targetSelector = ((MobAccessor) creeper).botania_getTargetSelector();
		for (var iterator = targetSelector.getAvailableGoals().iterator(); iterator.hasNext();) {
			WrappedGoal pg = iterator.next();
			if (pg.getGoal() instanceof NearestAttackableTargetGoalAccessor targetGoal
					&& targetGoal.botania_getTargetType() == Player.class) {
				iterator.remove();
				did = true;
			}
		}
		return did;
	}

	@Override
	public boolean triggerEvent(int id, int payload) {
		if (id == SUCCESS_EVENT) {
			if (level.isClientSide()) {
				Entity e = level.getEntity(payload);
				if (e != null) {
					int color = getColor();
					float r = FastColor.ARGB32.red(color) / 255f;
					float g = FastColor.ARGB32.green(color) / 255f;
					float b = FastColor.ARGB32.blue(color) / 255f;
					SparkleParticleData data = SparkleParticleData.sparkle(level.getRandom().nextFloat(), r, g, b, 10);

					for (int i = 0; i < 50; i++) {
						double x = e.getX() + level.getRandom().nextDouble() - 0.5;
						double y = e.getY() + e.getBbHeight() * level.getRandom().nextDouble();
						double z = e.getZ() + level.getRandom().nextDouble() - 0.5;
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
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
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
