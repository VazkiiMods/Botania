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
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibObfuscation;

import java.util.ArrayList;
import java.util.List;

public class SubTileHeiseiDream extends SubTileFunctional {

	private static final int RANGE = 5;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(supertile.getWorld().isRemote)
			return;

		final int cost = 100;

		@SuppressWarnings("unchecked")
		List<IMob> mobs = (List) supertile.getWorld().getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(supertile.getPos().add(-RANGE, -RANGE, -RANGE), supertile.getPos().add(RANGE + 1, RANGE + 1, RANGE + 1)), Predicates.instanceOf(IMob.class));

		if(mobs.size() > 1 && mana >= cost)
			for(IMob mob : mobs) {
				if(mob instanceof EntityLiving) {
					EntityLiving entity = (EntityLiving) mob;
					if(brainwashEntity(entity, mobs)) {
						mana -= cost;
						sync();
						break;
					}
				}
			}
	}

	public static boolean brainwashEntity(EntityLiving entity, List<IMob> mobs) {
		EntityLivingBase target = entity.getAttackTarget();
		boolean did = false;

		if(!(target instanceof IMob)) {
			IMob newTarget;
			do newTarget = mobs.get(entity.world.rand.nextInt(mobs.size()));
			while(newTarget == entity);

			if(newTarget instanceof EntityLiving) {
				entity.setAttackTarget(null);

				// Move any EntityAIHurtByTarget to highest priority
				for (EntityAITaskEntry entry : entity.targetTasks.taskEntries) {
					if (entry.action instanceof EntityAIHurtByTarget) {
						// Concurrent modification OK since we break out of the loop
						entity.targetTasks.removeTask(entry.action);
						entity.targetTasks.addTask(-1, entry.action);
						break;
					}
				}

				// Now set revenge target, which EntityAIHurtByTarget will pick up
				entity.setRevengeTarget((EntityLiving) newTarget);
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
