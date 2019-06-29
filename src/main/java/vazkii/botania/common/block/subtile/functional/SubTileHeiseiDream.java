/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 16, 2014, 12:37:40 AM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibMisc;

import java.util.List;

public class SubTileHeiseiDream extends TileEntityFunctionalFlower {
	@ObjectHolder(LibMisc.MOD_ID + ":heisei_dream")
	public static TileEntityType<SubTileHeiseiDream> TYPE;

	private static final int RANGE = 5;

	public SubTileHeiseiDream() {
		super(TYPE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if(getWorld().isRemote)
			return;

		final int cost = 100;

		@SuppressWarnings("unchecked")
		List<IMob> mobs = (List) getWorld().getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(getPos().add(-RANGE, -RANGE, -RANGE), getPos().add(RANGE + 1, RANGE + 1, RANGE + 1)), Predicates.instanceOf(IMob.class));

		if(mobs.size() > 1 && mana >= cost)
			for(IMob mob : mobs) {
				if(mob instanceof MobEntity) {
					MobEntity entity = (MobEntity) mob;
					if(brainwashEntity(entity, mobs)) {
						mana -= cost;
						sync();
						break;
					}
				}
			}
	}

	public static boolean brainwashEntity(MobEntity entity, List<IMob> mobs) {
		LivingEntity target = entity.getAttackTarget();
		boolean did = false;

		if(!(target instanceof IMob)) {
			IMob newTarget;
			do newTarget = mobs.get(entity.world.rand.nextInt(mobs.size()));
			while(newTarget == entity);

			if(newTarget instanceof MobEntity) {
				entity.setAttackTarget(null);

				// Move any EntityAIHurtByTarget to highest priority
				for (PrioritizedGoal entry : entity.targetSelector.goals) {
					if (entry.func_220772_j() instanceof HurtByTargetGoal) {
						// Concurrent modification OK since we break out of the loop
						entity.targetSelector.removeGoal(entry.func_220772_j());
						entity.targetSelector.addGoal(-1, entry.func_220772_j());
						break;
					}
				}

				// Now set revenge target, which EntityAIHurtByTarget will pick up
				entity.setRevengeTarget((MobEntity) newTarget);
				did = true;
			}
		}

		return did;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toBlockPos(), RANGE);
	}

	@Override
	public int getColor() {
		return 0xFF219D;
	}

	@Override
	public int getMaxMana() {
		return 1000;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.heiseiDream;
	}

}
