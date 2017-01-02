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

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Predicates;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
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

		if(target == null || !(target instanceof IMob)) {
			IMob newTarget;
			do newTarget = mobs.get(entity.world.rand.nextInt(mobs.size()));
			while(newTarget == entity);

			if(newTarget instanceof EntityLiving) {
				List<EntityAITaskEntry> entries = new ArrayList<>(entity.tasks.taskEntries);
				entries.addAll(new ArrayList<>(entity.targetTasks.taskEntries));

				for(EntityAITaskEntry entry : entries)
					if(entry.action instanceof EntityAINearestAttackableTarget) {
						messWithGetTargetAI((EntityAINearestAttackableTarget) entry.action, (EntityLiving) newTarget);
						did = true;
					} else if(entry.action instanceof EntityAIAttackMelee) {
						did = true;
					}

				if(did)
					entity.setAttackTarget((EntityLiving) newTarget);
			}
		}

		return did;
	}

	private static void messWithGetTargetAI(EntityAINearestAttackableTarget aiEntry, EntityLivingBase target) {
		ReflectionHelper.setPrivateValue(EntityAINearestAttackableTarget.class, aiEntry, Entity.class, LibObfuscation.TARGET_CLASS);
		ReflectionHelper.setPrivateValue(EntityAINearestAttackableTarget.class, aiEntry, Predicates.equalTo(target), LibObfuscation.TARGET_ENTITY_SELECTOR); // todo 1.8 will this leak `target`?
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
