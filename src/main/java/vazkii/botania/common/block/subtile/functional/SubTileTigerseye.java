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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.mixin.*;

import java.util.ArrayList;
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
				boolean did = false;

				GoalSelector goalSelector = ((AccessorMob) entity).getGoalSelector();
				Set<WrappedGoal> goals = ((AccessorGoalSelector) goalSelector).getAvailableGoals();
				boolean hasRunAwayFromPlayerGoal = goals.stream()
						.anyMatch(g -> g.getGoal() instanceof AvoidEntityGoal && ((AccessorAvoidEntityGoal) g.getGoal()).getClassToFleeFrom() == Player.class);
				if (!hasRunAwayFromPlayerGoal) {
					goalSelector.addGoal(3, new AvoidEntityGoal<>(entity, Player.class, 6, 1, 1.2));
					did = true;
				}

				GoalSelector targetSelector = ((AccessorMob) entity).getTargetSelector();
				for (WrappedGoal pg : new ArrayList<>(((AccessorGoalSelector) targetSelector).getAvailableGoals())) {
					if (pg.getGoal() instanceof NearestAttackableTargetGoal
							&& ((AccessorNearestAttackableTargetGoal) pg.getGoal()).getTargetClass() == Player.class) {
						targetSelector.removeGoal(pg.getGoal());
						did = true;
					}
				}

				if (did) {
					entity.playSound(ModSounds.tigerseyePacify, 1.0F, (level.random.nextFloat() - level.random.nextFloat()) * 0.2F + 1.0F);
					level.blockEvent(getBlockPos(), getBlockState().getBlock(), SUCCESS_EVENT, entity.getId());
					addMana(-COST);
					sync();
				}
			}
		}
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

}
