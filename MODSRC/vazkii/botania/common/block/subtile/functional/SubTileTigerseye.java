/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Feb 16, 2014, 3:36:26 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITaskEntry;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.subtile.SubTileFunctional;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibObfuscation;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class SubTileTigerseye extends SubTileFunctional {

	@Override
	public void onUpdate() {
		super.onUpdate();

		final int range = 10;
		final int rangeY = 4;
		final int cost = 70;
		
		boolean shouldAfffect = mana >= cost;
		
		List<EntityLiving> entities = supertile.worldObj.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(supertile.xCoord - range, supertile.yCoord - rangeY, supertile.zCoord - range, supertile.xCoord + range, supertile.yCoord + rangeY, supertile.zCoord + range));

		for(EntityLiving entity : entities) {
			List<EntityAITaskEntry> entries = new ArrayList(entity.tasks.taskEntries);
			entries.addAll(new ArrayList(entity.targetTasks.taskEntries));

			boolean avoidsOcelots = false;
			if(shouldAfffect)
				for(EntityAITaskEntry entry : entries) {
					if(entry.action instanceof EntityAIAvoidEntity)
						avoidsOcelots = messWithRunAwayAI((EntityAIAvoidEntity) entry.action) || avoidsOcelots;
	
					if(entry.action instanceof EntityAINearestAttackableTarget)
						messWithGetTargetAI((EntityAINearestAttackableTarget) entry.action);
				}
			
			if(entity instanceof EntityCreeper) {
				ReflectionHelper.setPrivateValue(EntityCreeper.class, (EntityCreeper) entity, 2, LibObfuscation.TIME_SINCE_IGNITED);
				entity.setAttackTarget(null);
			}
			
			if(avoidsOcelots) {
				mana -= cost;
				PacketDispatcher.sendPacketToAllInDimension(supertile.getDescriptionPacket(), supertile.worldObj.provider.dimensionId);
				shouldAfffect = false;
			}
		}
	}

	private boolean messWithRunAwayAI(EntityAIAvoidEntity aiEntry) {
		if(ReflectionHelper.getPrivateValue(EntityAIAvoidEntity.class, aiEntry, LibObfuscation.TARGET_ENTITY_CLASS) == EntityOcelot.class) {
			ReflectionHelper.setPrivateValue(EntityAIAvoidEntity.class, aiEntry, EntityPlayer.class, LibObfuscation.TARGET_ENTITY_CLASS);
			return true;
		}
		return false;
	}

	private void messWithGetTargetAI(EntityAINearestAttackableTarget aiEntry) {
		if(ReflectionHelper.getPrivateValue(EntityAINearestAttackableTarget.class, aiEntry, LibObfuscation.TARGET_CLASS) == EntityPlayer.class)
			ReflectionHelper.setPrivateValue(EntityAINearestAttackableTarget.class, aiEntry, EntityEnderCrystal.class, LibObfuscation.TARGET_CLASS); // Something random that won't be around
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
