/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;

import java.util.ArrayList;

public class SubTileTigerseye extends TileEntityFunctionalFlower {
	private static final int RANGE = 10;
	private static final int RANGE_Y = 4;
	private static final int COST = 70;

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
			entity.timeSinceIgnited = 2;
			entity.setAttackTarget(null);


			if (getMana() >= COST) {
				boolean did = false;

				boolean hasRunAwayFromPlayerGoal = entity.goalSelector.goals.stream()
					.anyMatch(g -> g.getGoal() instanceof AvoidEntityGoal && ((AvoidEntityGoal) g.getGoal()).classToAvoid == PlayerEntity.class);
				if (!hasRunAwayFromPlayerGoal) {
					entity.goalSelector.addGoal(3, new AvoidEntityGoal<>(entity, PlayerEntity.class, 6, 1, 1.2));
					did = true;
				}

				for (PrioritizedGoal pg : new ArrayList<>(entity.targetSelector.goals)) {
					if (pg.getGoal() instanceof NearestAttackableTargetGoal
						&& ((NearestAttackableTargetGoal) pg.getGoal()).targetClass == PlayerEntity.class) {
						entity.targetSelector.removeGoal(pg.getGoal());
						did = true;
					}
				}

				if (did) {
					addMana(-COST);
					sync();
				}
			}
		}
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
