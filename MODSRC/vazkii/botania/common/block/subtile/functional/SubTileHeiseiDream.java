/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Feb 16, 2014, 12:37:40 AM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.AxisAlignedBB;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibObfuscation;

import java.util.ArrayList;
import java.util.List;

public class SubTileHeiseiDream extends SubTileFunctional {

	@Override
	public void onUpdate() {
		super.onUpdate();

		final int range = 5;
		final int cost = 100;

		List<IMob> mobs = supertile.getWorldObj().getEntitiesWithinAABB(IMob.class, AxisAlignedBB.getBoundingBox(supertile.xCoord - range, supertile.yCoord - range, supertile.zCoord - range, supertile.xCoord + range, supertile.yCoord + range, supertile.zCoord + range));
		if(mobs.size() > 1 && mana >= cost)
			for(IMob mob : mobs) {
				if(mob instanceof EntityLiving) {
					EntityLiving entity = (EntityLiving) mob;
					EntityLivingBase target = entity.getAttackTarget();
					if(target == null || !(target instanceof IMob)) {
						IMob newTarget;
						do newTarget = mobs.get(supertile.getWorldObj().rand.nextInt(mobs.size()));
						while(newTarget == mob);

						if(newTarget instanceof EntityLivingBase) {
							entity.setAttackTarget((EntityLivingBase) newTarget);

							List<EntityAITaskEntry> entries = new ArrayList(entity.tasks.taskEntries);
							entries.addAll(new ArrayList(entity.targetTasks.taskEntries));

							for(EntityAITaskEntry entry : entries)
								if(entry.action instanceof EntityAINearestAttackableTarget)
									messWithGetTargetAI((EntityAINearestAttackableTarget) entry.action);
								else if(entry.action instanceof EntityAIAttackOnCollide)
									messWithAttackOnCollideAI((EntityAIAttackOnCollide) entry.action);

							mana -= cost;
							sync();
							break;
						}
					}
				}
			}
	}

	private void messWithGetTargetAI(EntityAINearestAttackableTarget aiEntry) {
		ReflectionHelper.setPrivateValue(EntityAINearestAttackableTarget.class, aiEntry, IMob.class, LibObfuscation.TARGET_CLASS);
	}

	private void messWithAttackOnCollideAI(EntityAIAttackOnCollide aiEntry) {
		ReflectionHelper.setPrivateValue(EntityAIAttackOnCollide.class, aiEntry, IMob.class, LibObfuscation.CLASS_TARGET);
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
