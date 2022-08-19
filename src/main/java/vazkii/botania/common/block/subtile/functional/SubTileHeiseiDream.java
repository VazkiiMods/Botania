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

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.AxisAlignedBB;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibObfuscation;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class SubTileHeiseiDream extends SubTileFunctional {

	private static final int RANGE = 5;

	@Override
	public void onUpdate() {
		super.onUpdate();

		final int cost = 100;

		List<IMob> mobs = supertile.getWorldObj().getEntitiesWithinAABB(IMob.class, AxisAlignedBB.getBoundingBox(supertile.xCoord - RANGE, supertile.yCoord - RANGE, supertile.zCoord - RANGE, supertile.xCoord + RANGE + 1, supertile.yCoord + RANGE + 1, supertile.zCoord + RANGE + 1));
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
			do newTarget = mobs.get(entity.worldObj.rand.nextInt(mobs.size()));
			while(newTarget == entity);

			if(newTarget instanceof EntityLiving) {
				List<EntityAITaskEntry> entries = new ArrayList(entity.tasks.taskEntries);
				entries.addAll(new ArrayList(entity.targetTasks.taskEntries));

				for(EntityAITaskEntry entry : entries)
					if(entry.action instanceof EntityAINearestAttackableTarget) {
						messWithGetTargetAI((EntityAINearestAttackableTarget) entry.action, (EntityLiving) newTarget);
						did = true;
					} else if(entry.action instanceof EntityAIAttackOnCollide) {
						messWithAttackOnCollideAI((EntityAIAttackOnCollide) entry.action);
						did = true;
					}

				if(did)
					entity.setAttackTarget((EntityLiving) newTarget);
			}
		}

		return did;
	}

	private static void messWithGetTargetAI(EntityAINearestAttackableTarget aiEntry, EntityLivingBase target) {
		ReflectionHelper.setPrivateValue(EntityAINearestAttackableTarget.class, aiEntry, IMob.class, LibObfuscation.TARGET_CLASS);
		ReflectionHelper.setPrivateValue(EntityAINearestAttackableTarget.class, aiEntry, target, LibObfuscation.TARGET_ENTITY);
	}

	private static void messWithAttackOnCollideAI(EntityAIAttackOnCollide aiEntry) {
		ReflectionHelper.setPrivateValue(EntityAIAttackOnCollide.class, aiEntry, IMob.class, LibObfuscation.CLASS_TARGET);
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toChunkCoordinates(), RANGE);
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
