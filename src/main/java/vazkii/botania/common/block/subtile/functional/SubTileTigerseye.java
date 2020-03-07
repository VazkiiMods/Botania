/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.item.EnderCrystalEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.lib.LibMisc;

import java.util.ArrayList;
import java.util.List;

public class SubTileTigerseye extends TileEntityFunctionalFlower {
	@ObjectHolder(LibMisc.MOD_ID + ":tigerseye") public static TileEntityType<SubTileTigerseye> TYPE;

	private static final int RANGE = 10;
	private static final int RANGE_Y = 4;
	private static final int COST = 70;

	public SubTileTigerseye() {
		super(TYPE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getWorld().isRemote) {
			return;
		}

		boolean shouldAfffect = getMana() >= COST;

		List<MobEntity> entities = getWorld().getEntitiesWithinAABB(MobEntity.class, new AxisAlignedBB(getEffectivePos().add(-RANGE, -RANGE_Y, -RANGE), getEffectivePos().add(RANGE + 1, RANGE_Y + 1, RANGE + 1)));

		for (MobEntity entity : entities) {
			List<PrioritizedGoal> entries = new ArrayList<>(entity.goalSelector.goals);
			entries.addAll(entity.targetSelector.goals);

			boolean avoidsOcelots = false;
			if (shouldAfffect) {
				for (PrioritizedGoal entry : entries) {
					if (entry.getGoal() instanceof AvoidEntityGoal) {
						avoidsOcelots = messWithRunAwayAI((AvoidEntityGoal) entry.getGoal()) || avoidsOcelots;
					}

					if (entry.getGoal() instanceof NearestAttackableTargetGoal) {
						messWithGetTargetAI((NearestAttackableTargetGoal) entry.getGoal());
					}
				}
			}

			if (entity instanceof CreeperEntity) {
				((CreeperEntity) entity).timeSinceIgnited = 2;
				entity.setAttackTarget(null);
			}

			if (avoidsOcelots) {
				addMana(-COST);
				sync();
				shouldAfffect = false;
			}
		}
	}

	private boolean messWithRunAwayAI(AvoidEntityGoal aiEntry) {
		if (aiEntry.classToAvoid == OcelotEntity.class) {
			aiEntry.classToAvoid = PlayerEntity.class;
			return true;
		}
		return false;
	}

	private void messWithGetTargetAI(NearestAttackableTargetGoal aiEntry) {
		if (aiEntry.targetClass == PlayerEntity.class) {
			aiEntry.targetClass = EnderCrystalEntity.class; // Something random that won't be around
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
