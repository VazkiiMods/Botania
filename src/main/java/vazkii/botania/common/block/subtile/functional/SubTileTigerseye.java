/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 16, 2014, 3:36:26 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibObfuscation;

import java.util.ArrayList;
import java.util.List;

public class SubTileTigerseye extends SubTileFunctional {

	private static final int RANGE = 10;
	private static final int RANGE_Y = 4;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(supertile.getWorld().isRemote)
			return;

		final int cost = 70;

		boolean shouldAfffect = mana >= cost;

		List<EntityLiving> entities = supertile.getWorld().getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(supertile.getPos().add(-RANGE, -RANGE_Y, -RANGE), supertile.getPos().add(RANGE + 1, RANGE_Y + 1, RANGE + 1)));

		for(EntityLiving entity : entities) {
			List<EntityAITaskEntry> entries = new ArrayList<>(entity.tasks.taskEntries);
			entries.addAll(entity.targetTasks.taskEntries);

			boolean avoidsOcelots = false;
			if(shouldAfffect)
				for(EntityAITaskEntry entry : entries) {
					if(entry.action instanceof EntityAIAvoidEntity)
						avoidsOcelots = messWithRunAwayAI((EntityAIAvoidEntity) entry.action) || avoidsOcelots;

					if(entry.action instanceof EntityAINearestAttackableTarget)
						messWithGetTargetAI((EntityAINearestAttackableTarget) entry.action);
				}

			if(entity instanceof EntityCreeper) {
				((EntityCreeper) entity).timeSinceIgnited = 2;
				entity.setAttackTarget(null);
			}

			if(avoidsOcelots) {
				mana -= cost;
				sync();
				shouldAfffect = false;
			}
		}
	}

	private boolean messWithRunAwayAI(EntityAIAvoidEntity aiEntry) {
		if(aiEntry.classToAvoid == EntityOcelot.class) {
			aiEntry.classToAvoid = EntityPlayer.class;
			return true;
		}
		return false;
	}

	private void messWithGetTargetAI(EntityAINearestAttackableTarget aiEntry) {
		if(aiEntry.targetClass == EntityPlayer.class)
			aiEntry.targetClass = EntityEnderCrystal.class; // Something random that won't be around
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toBlockPos(), RANGE);
	}

	@Override
	public int getColor() {
		return 0xB1A618;
	}

	@Override
	public int getMaxMana() {
		return 1000;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.tigerseye;
	}

}
